package com.petcare.back.service;

import com.petcare.back.domain.dto.request.ScheduleConfigCreateDTO;
import com.petcare.back.domain.dto.response.ScheduleConfigResponseDTO;
import com.petcare.back.domain.entity.Schedule;
import com.petcare.back.domain.entity.ScheduleConfig;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.domain.enumerated.WeekDayEnum;
import com.petcare.back.domain.mapper.request.ScheduleConfigCreateMapper;
import com.petcare.back.domain.mapper.response.ScheduleConfigResponseMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.ScheduleConfigRepository;
import com.petcare.back.repository.ScheduleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.*;

@Service
@RequiredArgsConstructor
public class ScheduleConfigService {

    private final ScheduleConfigRepository configRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleConfigCreateMapper configMapper;
    private final ScheduleConfigResponseMapper configResponseMapper;

    public ScheduleConfigResponseDTO createScheduleConfig(ScheduleConfigCreateDTO dto) throws MyException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user.getRole() != Role.SITTER) {
            throw new MyException("Solo los profesionales pueden configurar sus horarios");
        };

        ScheduleConfig config = configMapper.toEntity(dto);
        config.setSitter(user);
        configRepository.save(config);

        int count = generateSchedules(config);

        ScheduleConfigResponseDTO base = configResponseMapper.toDto(config);
        return new ScheduleConfigResponseDTO(
                base.id(),
                base.configurationName(),
                user.getName(),
                base.startDate(),
                base.endDate(),
                base.days(),
                count
        );
    }

    private int generateSchedules(ScheduleConfig config) {
        int count = 0;
        LocalDate currentDate = config.getStartDate();
        while (!currentDate.isAfter(config.getEndDate())) {
            DayOfWeek day = currentDate.getDayOfWeek();
            if (config.getDays().contains(WeekDayEnum.fromDayOfWeek(day))) {
                LocalTime time = config.getStartTime();
                while (time.plusMinutes(config.getServiceDurationMinutes()).isBefore(config.getEndTime()) ||
                        time.plusMinutes(config.getServiceDurationMinutes()).equals(config.getEndTime())) {

                    if (isInBreak(time, config)) {
                        time = config.getBreakEnd();
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

    private boolean isInBreak(LocalTime time, ScheduleConfig config) {
        return config.getBreakStart() != null && config.getBreakEnd() != null &&
                !time.isBefore(config.getBreakStart()) && time.isBefore(config.getBreakEnd());
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
}
