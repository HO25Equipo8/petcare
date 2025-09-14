package com.petcare.back.service;

import com.petcare.back.domain.dto.request.ScheduleConfigCreateDTO;
import com.petcare.back.domain.dto.request.ScheduleRescheduleDTO;
import com.petcare.back.domain.dto.response.*;
import com.petcare.back.domain.entity.Schedule;
import com.petcare.back.domain.entity.ScheduleConfig;
import com.petcare.back.domain.entity.ScheduleTurn;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.domain.enumerated.ScheduleStatus;
import com.petcare.back.domain.enumerated.WeekDayEnum;
import com.petcare.back.domain.mapper.request.ScheduleConfigCreateMapper;
import com.petcare.back.domain.mapper.request.ScheduleTurnRequestMapper;
import com.petcare.back.domain.mapper.response.ScheduleResponseMapper;
import com.petcare.back.domain.mapper.response.ScheduleTurnResponseMapper;
import com.petcare.back.domain.mapper.response.ScheduleWithSitterMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.BookingRepository;
import com.petcare.back.repository.ScheduleConfigRepository;
import com.petcare.back.repository.ScheduleRepository;
import com.petcare.back.validation.ValidationScheduleConfig;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.Map;
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
    private final ScheduleResponseMapper scheduleMapper;;
    private final ScheduleWithSitterMapper scheduleWithSitterMapper;

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

    public List<ScheduleConfigResponseDTO> getAllConfigsBySitter() throws MyException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        List<ScheduleConfig> configs = configRepository.findBySitterId(user.getId());

        if (user.getRole() != Role.SITTER) {
            throw new MyException("Solo los profesionales pueden configurar sus horarios");
        }

        return configs.stream()
                .map(config -> new ScheduleConfigResponseDTO(
                        config.getId(),
                        config.getConfigurationName(),
                        user.getName(),
                        config.getStartDate(),
                        config.getEndDate(),
                        config.getTurns().stream()
                                .map(ScheduleTurn::getDay)
                                .distinct()
                                .sorted()
                                .toList(),
                        scheduleTurnResponseMapper.toDtoList(config.getTurns()),
                        scheduleRepository.countByScheduleConfig(config)
                ))
                .toList();
    }

    @Transactional
    public void deactivateConfig(Long id, User sitter) throws MyException {
        ScheduleConfig config = configRepository.findById(id)
                .orElseThrow(() -> new MyException("Configuración no encontrada"));

        if (!config.getSitter().getId().equals(sitter.getId())) {
            throw new MyException("No tenés permiso para eliminar esta configuración");
        }

        config.setActive(false);
        configRepository.save(config);

        List<Schedule> futurosSinReserva = scheduleRepository.findFutureUnlinkedByConfig(config.getId());

        futurosSinReserva.forEach(s -> s.setStatus(ScheduleStatus.BLOQUEADO));
        scheduleRepository.saveAll(futurosSinReserva);
    }


    @Transactional
    public ScheduleConfigResponseDTO activateConfig(Long id, User sitter) throws MyException {
        ScheduleConfig config = configRepository.findById(id)
                .orElseThrow(() -> new MyException("Configuración no encontrada"));

        if (!config.getSitter().getId().equals(sitter.getId())) {
            throw new MyException("No tenés permiso para activar esta configuración");
        }

        if (config.isActive()) {
            throw new MyException("Esta configuración ya está activa");
        }

        config.setActive(true);
        configRepository.save(config);

        // Restaurar horarios bloqueados
        List<Schedule> bloqueados = scheduleRepository.findFutureBlockedByConfig(config.getId());
        bloqueados.forEach(s -> s.setStatus(ScheduleStatus.DISPONIBLE));
        scheduleRepository.saveAll(bloqueados);

        // Generar nuevos si no hay suficientes
        int count = generateSchedules(config);

        List<WeekDayEnum> days = config.getTurns().stream()
                .map(ScheduleTurn::getDay)
                .distinct()
                .sorted()
                .toList();

        List<ScheduleTurnResponseDTO> turnDtos = scheduleTurnResponseMapper.toDtoList(config.getTurns());

        return new ScheduleConfigResponseDTO(
                config.getId(),
                config.getConfigurationName(),
                sitter.getName(),
                config.getStartDate(),
                config.getEndDate(),
                days,
                turnDtos,
                count
        );
    }
    @Transactional
    public ScheduleResponseDTO reschedule(Long scheduleId, ScheduleRescheduleDTO dto, User sitter) throws MyException {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new MyException("Horario no encontrado"));

        if (!schedule.getScheduleConfig().getSitter().getId().equals(sitter.getId())) {
            throw new MyException("No tenés permiso para modificar este horario");
        }

        if (schedule.getStatus() != ScheduleStatus.DISPONIBLE) {
            throw new MyException("Solo se pueden reprogramar horarios disponibles");
        }

        schedule.setEstablishedTime(dto.newTime());
        schedule.setStatus(ScheduleStatus.PENDIENTE); // o DISPONIBLE si querés mantenerlo abierto
        scheduleRepository.save(schedule);

        WeekDayEnum day = WeekDayEnum.valueOf(
                LocalDateTime.ofInstant(schedule.getEstablishedTime(), ZoneId.systemDefault())
                        .getDayOfWeek().name()
        );

        return new ScheduleResponseDTO(
                schedule.getScheduleId(),
                schedule.getEstablishedTime(),
                schedule.getStatus(),
                day,
                schedule.getScheduleConfig().getConfigurationName()
        );
    }

    public List<Map<String, Object>> getVisualBlocks() throws MyException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user.getRole() != Role.SITTER) {
            throw new MyException("Solo los profesionales pueden ver su configuración de horarios");
        }
        List<ScheduleConfig> configs = configRepository.findBySitterId(user.getId());

        return configs.stream().map(config -> {
            List<WeekDayEnum> dias = config.getTurns().stream()
                    .map(ScheduleTurn::getDay)
                    .distinct()
                    .sorted()
                    .toList();

            int cantidadHorarios = scheduleRepository.countByScheduleConfig(config);

            return Map.of(
                    "id", config.getId(),
                    "nombre", config.getConfigurationName(),
                    "rango", config.getStartDate() + " → " + config.getEndDate(),
                    "días", dias,
                    "cantidadHorarios", cantidadHorarios,
                    "estado", config.isActive() ? "Activa" : "Inactiva"
            );
        }).toList();
    }

    @Transactional
    public ScheduleConfigResponseDTO duplicateConfig(Long id, User sitter) throws MyException {
        ScheduleConfig original = configRepository.findById(id)
                .orElseThrow(() -> new MyException("Configuración no encontrada"));

        if (!original.getSitter().getId().equals(sitter.getId())) {
            throw new MyException("No tenés permiso para duplicar esta configuración");
        }

        ScheduleConfig nueva = new ScheduleConfig();
        nueva.setSitter(sitter);
        nueva.setConfigurationName(original.getConfigurationName() + " (duplicada)");
        nueva.setStartDate(LocalDate.now());
        nueva.setEndDate(original.getEndDate());
        nueva.setServiceDurationMinutes(original.getServiceDurationMinutes());
        nueva.setIntervalBetweenServices(original.getIntervalBetweenServices());
        nueva.setActive(true);

        List<ScheduleTurn> nuevosTurnos = original.getTurns().stream()
                .map(turn -> {
                    ScheduleTurn nuevo = new ScheduleTurn();
                    nuevo.setDay(turn.getDay());
                    nuevo.setStartTime(turn.getStartTime());
                    nuevo.setEndTime(turn.getEndTime());
                    nuevo.setBreakStart(turn.getBreakStart());
                    nuevo.setBreakEnd(turn.getBreakEnd());
                    nuevo.setScheduleConfig(nueva);
                    return nuevo;
                }).toList();

        nueva.setTurns(nuevosTurnos);
        configRepository.save(nueva);

        int count = generateSchedules(nueva);

        List<ScheduleTurnResponseDTO> turnDtos = scheduleTurnResponseMapper.toDtoList(nuevosTurnos);
        List<WeekDayEnum> days = nuevosTurnos.stream().map(ScheduleTurn::getDay).distinct().sorted().toList();

        return new ScheduleConfigResponseDTO(
                nueva.getId(),
                nueva.getConfigurationName(),
                sitter.getName(),
                nueva.getStartDate(),
                nueva.getEndDate(),
                days,
                turnDtos,
                count
        );
    }

    public Page<ScheduleResponseDTO> getFilteredSchedulesForSitter(User sitter,
                                                                   ScheduleStatus status,
                                                                   WeekDayEnum dayEnum,
                                                                   Long configId,
                                                                   LocalDate from,
                                                                   LocalDate to,
                                                                   Pageable pageable) throws MyException {

        if (status == null && dayEnum == null && configId == null && from == null && to == null) {
            return scheduleRepository.findBySitterId(sitter.getId(), pageable)
                    .map(scheduleMapper::toDto);
        }

        if (status != null && dayEnum != null) {
            int dayInt = mapEnumToDayOfWeek(dayEnum);
            return scheduleRepository.findBySitterAndStatusAndDay(sitter.getId(), status, dayInt, pageable)
                    .map(scheduleMapper::toDto);
        }

        if (status != null) {
            return scheduleRepository.findBySitterAndStatus(sitter.getId(), status, pageable)
                    .map(scheduleMapper::toDto);
        }

        if (dayEnum != null) {
            int dayInt = mapEnumToDayOfWeek(dayEnum);
            return scheduleRepository.findBySitterAndDay(sitter.getId(), dayInt, pageable)
                    .map(scheduleMapper::toDto);
        }

        if (configId != null) {
            return scheduleRepository.findBySitterAndConfig(sitter.getId(), configId, pageable)
                    .map(scheduleMapper::toDto);
        }

        if (from != null && to != null) {
            Instant fromInstant = from.atStartOfDay(ZoneId.systemDefault()).toInstant();
            Instant toInstant = to.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant();
            return scheduleRepository.findBySitterAndDateRange(sitter.getId(), fromInstant, toInstant, pageable)
                    .map(scheduleMapper::toDto);
        }

        throw new MyException("Combinación de filtros inválida para cuidador.");
    }


    public Page<ScheduleWithSitterDTO> getFilteredSchedulesForOwner(User owner,
                                                                    ScheduleStatus status,
                                                                    WeekDayEnum dayEnum,
                                                                    LocalDate from,
                                                                    LocalDate to,
                                                                    Pageable pageable) throws MyException {

        if (status == null && dayEnum == null && from == null && to == null) {
            return scheduleRepository.findAll(pageable)
                    .map(scheduleWithSitterMapper::toDto);
        }

        if (status != null && dayEnum != null) {
            int dayInt = dayEnum.toDayOfWeek().getValue();
            return scheduleRepository.findByStatusAndDay(status, dayInt, pageable)
                    .map(scheduleWithSitterMapper::toDto);
        }

        if (status != null) {
            return scheduleRepository.findByStatus(status, pageable)
                    .map(scheduleWithSitterMapper::toDto);
        }

        if (dayEnum != null) {
            int dayInt = dayEnum.toDayOfWeek().getValue();
            return scheduleRepository.findByDay(dayInt, pageable)
                    .map(scheduleWithSitterMapper::toDto);
        }

        if (from != null && to != null) {
            if (from.isAfter(to)) {
                throw new MyException("La fecha 'desde' no puede ser posterior a la fecha 'hasta'.");
            }
            Instant fromInstant = from.atStartOfDay(ZoneId.systemDefault()).toInstant();
            Instant toInstant = to.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant();
            return scheduleRepository.findByDateRange(fromInstant, toInstant, pageable)
                    .map(scheduleWithSitterMapper::toDto);
        }

        throw new MyException("Combinación de filtros inválida para dueño.");
    }


    public Page<ScheduleWithSitterDTO> getFilteredSchedulesForAdmin(ScheduleStatus status,
                                                                    WeekDayEnum dayEnum,
                                                                    Long configId,
                                                                    LocalDate from,
                                                                    LocalDate to,
                                                                    Pageable pageable) throws MyException {

        // 🔹 Mostrar todos si no hay filtros
        if (status == null && dayEnum == null && configId == null && from == null && to == null) {
            return scheduleRepository.findAll(pageable)
                    .map(scheduleWithSitterMapper::toDto);
        }

        // 🔹 Filtro combinado: estado + día
        if (status != null && dayEnum != null) {
            int dayInt = mapEnumToDayOfWeek(dayEnum);
            return scheduleRepository.findByStatusAndDay(status, dayInt, pageable)
                    .map(scheduleWithSitterMapper::toDto);
        }

        // 🔹 Filtro por estado
        if (status != null) {
            return scheduleRepository.findByStatus(status, pageable)
                    .map(scheduleWithSitterMapper::toDto);
        }

        // 🔹 Filtro por día
        if (dayEnum != null) {
            int dayInt = mapEnumToDayOfWeek(dayEnum);
            return scheduleRepository.findByDay(dayInt, pageable)
                    .map(scheduleWithSitterMapper::toDto);
        }

        // 🔹 Filtro por configuración
        if (configId != null) {
            return scheduleRepository.findByConfig(configId, pageable)
                    .map(scheduleWithSitterMapper::toDto);
        }

        // 🔹 Filtro por rango de fechas
        if (from != null && to != null) {
            Instant fromInstant = from.atStartOfDay(ZoneId.systemDefault()).toInstant();
            Instant toInstant = to.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant();
            return scheduleRepository.findByDateRange(fromInstant, toInstant, pageable)
                    .map(scheduleWithSitterMapper::toDto);
        }

        throw new MyException("Combinación de filtros inválida para administrador.");
    }

    private int mapEnumToDayOfWeek(WeekDayEnum dayEnum) {
        return switch (dayEnum) {
            case DOMINGO -> 1;
            case LUNES -> 2;
            case MARTES -> 3;
            case MIERCOLES -> 4;
            case JUEVES -> 5;
            case VIERNES -> 6;
            case SABADO -> 7;
        };
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
