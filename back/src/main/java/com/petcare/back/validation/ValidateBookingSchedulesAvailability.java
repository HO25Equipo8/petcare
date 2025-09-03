package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.domain.entity.Schedule;
import com.petcare.back.domain.enumerated.ScheduleStatus;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.ScheduleRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
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

        Map<ScheduleStatus, List<Long>> agrupados = schedules.stream()
                .filter(s -> s.getStatus() != ScheduleStatus.DISPONIBLE)
                .collect(Collectors.groupingBy(Schedule::getStatus,
                        Collectors.mapping(Schedule::getScheduleId, Collectors.toList())));

        if (!agrupados.isEmpty()) {
            String mensaje = agrupados.entrySet().stream()
                    .map(e -> e.getKey().name() + ": " + e.getValue())
                    .collect(Collectors.joining(" | "));
            throw new MyException("Horarios no disponibles → " + mensaje);
        }
    }
}

