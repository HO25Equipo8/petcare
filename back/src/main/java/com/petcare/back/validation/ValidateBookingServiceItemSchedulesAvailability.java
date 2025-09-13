package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.domain.dto.request.BookingServiceItemCreateDTO;
import com.petcare.back.domain.entity.Schedule;
import com.petcare.back.domain.enumerated.ScheduleStatus;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.ScheduleRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ValidateBookingServiceItemSchedulesAvailability implements ValidationBooking {

    private final ScheduleRepository scheduleRepository;

    public ValidateBookingServiceItemSchedulesAvailability(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public void validate(BookingCreateDTO data) throws MyException {
        if (data.items() == null || data.items().isEmpty()) return;

        List<Long> scheduleIds = data.items().stream()
                .map(BookingServiceItemCreateDTO::scheduleId)
                .filter(Objects::nonNull)
                .toList();

        Map<Long, Schedule> scheduleMap = scheduleRepository.findAllById(scheduleIds).stream()
                .collect(Collectors.toMap(Schedule::getScheduleId, s -> s));

        Map<ScheduleStatus, List<Long>> noDisponibles = scheduleMap.values().stream()
                .filter(s -> s.getStatus() != ScheduleStatus.DISPONIBLE)
                .collect(Collectors.groupingBy(Schedule::getStatus,
                        Collectors.mapping(Schedule::getScheduleId, Collectors.toList())));

        if (!noDisponibles.isEmpty()) {
            String mensaje = noDisponibles.entrySet().stream()
                    .map(e -> e.getKey().name() + ": " + e.getValue())
                    .collect(Collectors.joining(" | "));
            throw new MyException("Horarios no disponibles → " + mensaje);
        }
    }
}