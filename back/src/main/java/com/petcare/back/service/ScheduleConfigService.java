package com.petcare.back.service;

import com.petcare.back.domain.dto.request.ScheduleConfigCreateDTO;
import com.petcare.back.domain.dto.response.ScheduleConfigResponseDTO;
import com.petcare.back.domain.dto.response.ScheduleConfigStatusResponseDTO;
import com.petcare.back.domain.dto.response.ScheduleTurnResponseDTO;
import com.petcare.back.domain.entity.Schedule;
import com.petcare.back.domain.entity.ScheduleConfig;
import com.petcare.back.domain.entity.ScheduleTurn;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.domain.enumerated.WeekDayEnum;
import com.petcare.back.domain.mapper.request.ScheduleConfigCreateMapper;
import com.petcare.back.domain.mapper.request.ScheduleTurnRequestMapper;
import com.petcare.back.domain.mapper.response.ScheduleTurnResponseMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.ScheduleConfigRepository;
import com.petcare.back.repository.ScheduleRepository;
import com.petcare.back.validation.ValidationScheduleConfig;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleConfigService {

    private final ScheduleConfigRepository configRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleConfigCreateMapper configMapper;
    private final ScheduleTurnResponseMapper scheduleTurnResponseMapper;
    private final ScheduleTurnRequestMapper scheduleTurnRequestMapper;
    private final List<ValidationScheduleConfig> validations;

    public ScheduleConfigResponseDTO createScheduleConfig(ScheduleConfigCreateDTO dto) throws MyException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user.getRole() != Role.SITTER) {
            throw new MyException("Solo los profesionales pueden configurar sus horarios");
        }

        if(!user.isVerified()){
            if (user.getProfileComplete()) {
                throw new MyException("Debes estar verificado para poder crear combos, tu perfil está en evaluación");
            }else{
                throw new MyException("Debes completa tu perfil para poder estar verificado");
            }
        }

        for (ValidationScheduleConfig v : validations) {
            v.validate(dto);
        }

        ScheduleConfig config = configMapper.toEntity(dto);
        config.setSitter(user);

        List<ScheduleTurn> turns = dto.turns().stream()
                .map(turnDto -> {
                    ScheduleTurn turn = scheduleTurnRequestMapper.toEntity(turnDto);
                    turn.setScheduleConfig(config);
                    return turn;
                })
                .toList();

        config.setTurns(turns);
        configRepository.save(config);

        int count = generateSchedules(config);

        List<WeekDayEnum> days = turns.stream()
                .map(ScheduleTurn::getDay)
                .distinct()
                .sorted()
                .toList();

        List<ScheduleTurnResponseDTO> turnDtos = scheduleTurnResponseMapper.toDtoList(turns);

        return new ScheduleConfigResponseDTO(
                config.getId(),
                config.getConfigurationName(),
                user.getName(),
                config.getStartDate(),
                config.getEndDate(),
                days,
                turnDtos,
                count
        );
    }

    private int generateSchedules(ScheduleConfig config) {
        int count = 0;
        LocalDate currentDate = config.getStartDate();

        while (!currentDate.isAfter(config.getEndDate())) {
            WeekDayEnum currentDay = WeekDayEnum.fromDayOfWeek(currentDate.getDayOfWeek());

            List<ScheduleTurn> turnsForDay = config.getTurns().stream()
                    .filter(turn -> turn.getDay() == currentDay)
                    .toList();

            for (ScheduleTurn turn : turnsForDay) {
                LocalTime time = turn.getStartTime();

                while (!time.plusMinutes(config.getServiceDurationMinutes()).isAfter(turn.getEndTime())) {

                    if (isInBreak(time, turn)) {
                        time = turn.getBreakEnd();
                        continue;
                    }

                    Instant establishedTime = LocalDateTime.of(currentDate, time)
                            .atZone(ZoneId.systemDefault()).toInstant();

                    Schedule schedule = new Schedule();
                    schedule.setEstablishedTime(establishedTime);
                    schedule.setScheduleConfig(config);
                    scheduleRepository.save(schedule);

                    count++;
                    time = time.plusMinutes(config.getServiceDurationMinutes() + config.getIntervalBetweenServices());
                }
            }

            currentDate = currentDate.plusDays(1);
        }

        return count;
    }


    private boolean isInBreak(LocalTime time, ScheduleTurn turn) {
        return turn.getBreakStart() != null && turn.getBreakEnd() != null &&
                !time.isBefore(turn.getBreakStart()) && time.isBefore(turn.getBreakEnd());
    }

    public ScheduleConfigStatusResponseDTO getScheduleStatus() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        Optional<ScheduleConfig> config = configRepository.findBySitterAndActiveTrue(user);

        if (config.isPresent()) {
            return new ScheduleConfigStatusResponseDTO(true, config.get().getEndDate());
        } else {
            return new ScheduleConfigStatusResponseDTO(false, null);
        }
    }

    private static final Logger log = LoggerFactory.getLogger(ScheduleConfigService.class);
    @Scheduled(cron = "0 0 2 * * *") // Cada día a las 2 AM
    @Transactional
    public int expireOldSchedules() {
        Instant today = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant();
        int count = scheduleRepository.expireSchedulesBefore(today);
        log.info("Se expiraron {} horarios antiguos disponibles", count);
        return count;
    }

    @Scheduled(cron = "0 10 2 * * *") // 10 minutos después
    @Transactional
    public int deleteUnlinkedExpiredSchedules() {
        List<Schedule> expiradosSinReserva = scheduleRepository.findExpiredWithoutBookings();

        scheduleRepository.deleteAll(expiradosSinReserva);
        log.info("Se eliminaron {} horarios expirados sin reservas", expiradosSinReserva.size());

        return expiradosSinReserva.size();
    }

    @Scheduled(cron = "0 0 1 * * *") // Cada día a la 1 AM
    @Transactional
    public void deactivateExpiredConfigs() {
        LocalDate today = LocalDate.now();
        List<ScheduleConfig> expiredConfigs = configRepository.findByActiveTrueAndEndDateBefore(today);

        expiredConfigs.forEach(config -> config.setActive(false));
        configRepository.saveAll(expiredConfigs);

        log.info("Se desactivaron {} configuraciones vencidas", expiredConfigs.size());
    }
}
