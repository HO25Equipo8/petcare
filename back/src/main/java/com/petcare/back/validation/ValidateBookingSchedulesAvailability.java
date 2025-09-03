package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.domain.entity.Schedule;
import com.petcare.back.domain.enumerated.ScheduleStatus;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.ScheduleRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ValidateBookingSchedulesAvailability implements ValidationBooking {

    private final ScheduleRepository scheduleRepository;

    public ValidateBookingSchedulesAvailability(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public void validate(BookingCreateDTO data) throws MyException {
        List<Schedule> schedules = scheduleRepository.findAllById(data.scheduleIds());

        List<Schedule> unavailable = schedules.stream()
                .filter(s -> s.getStatus() != ScheduleStatus.DISPONIBLE)
                .toList();

        if (!unavailable.isEmpty()) {
            String ids = unavailable.stream()
                    .map(s -> String.valueOf(s.getScheduleId()))
                    .collect(Collectors.joining(", "));
            throw new MyException("Los siguientes horarios no están disponibles: " + ids);
        }
    }
}
