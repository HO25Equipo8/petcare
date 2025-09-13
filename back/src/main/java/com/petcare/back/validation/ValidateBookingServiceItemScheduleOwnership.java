package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.domain.dto.request.BookingServiceItemCreateDTO;
import com.petcare.back.domain.entity.Schedule;
import com.petcare.back.domain.entity.User;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.ScheduleRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ValidateBookingServiceItemScheduleOwnership implements ValidationBooking {

    private final ScheduleRepository scheduleRepository;

    public ValidateBookingServiceItemScheduleOwnership(ScheduleRepository scheduleRepository) {
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

        List<Long> horariosInvalidos = new ArrayList<>();

        for (BookingServiceItemCreateDTO item : data.items()) {
            Long scheduleId = item.scheduleId();
            Long professionalId = item.professionalId();

            if (scheduleId == null || professionalId == null) continue;

            Schedule schedule = scheduleMap.get(scheduleId);
            if (schedule == null || schedule.getScheduleConfig() == null) {
                horariosInvalidos.add(scheduleId);
                continue;
            }

            User sitter = schedule.getScheduleConfig().getSitter();
            if (sitter == null || !sitter.getId().equals(professionalId)) {
                horariosInvalidos.add(scheduleId);
            }
        }

        if (!horariosInvalidos.isEmpty()) {
            throw new MyException("Los siguientes horarios no pertenecen a los profesionales asignados: " + horariosInvalidos);
        }
    }
}
