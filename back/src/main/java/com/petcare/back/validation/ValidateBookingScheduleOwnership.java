package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.domain.entity.Schedule;
import com.petcare.back.domain.entity.User;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.ScheduleRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValidateBookingScheduleOwnership implements ValidationBooking {

    private final ScheduleRepository scheduleRepository;

    public ValidateBookingScheduleOwnership(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public void validate(BookingCreateDTO data) throws MyException {
        if (data.scheduleIds() == null || data.professionals() == null) return;

        List<Schedule> schedules = scheduleRepository.findAllById(data.scheduleIds());
        List<Long> profesionalesIds = data.professionals();

        List<Long> horariosInvalidos = schedules.stream()
                .filter(s -> {
                    User sitter = s.getScheduleConfig().getSitter();
                    return sitter == null || !profesionalesIds.contains(sitter.getId());
                })
                .map(Schedule::getScheduleId)
                .toList();

        if (!horariosInvalidos.isEmpty()) {
            throw new MyException("Los siguientes horarios no pertenecen a los profesionales seleccionados: " + horariosInvalidos);
        }
    }
}
