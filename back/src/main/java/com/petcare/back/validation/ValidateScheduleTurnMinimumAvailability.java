package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.ScheduleConfigCreateDTO;
import com.petcare.back.domain.dto.request.ScheduleTurnCreateDTO;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@Component
public class ValidateScheduleTurnMinimumAvailability implements ValidationScheduleConfig {

    @Override
    public void validate(ScheduleConfigCreateDTO dto) throws MyException {
        for (ScheduleTurnCreateDTO turn : dto.turns()) {
            long totalMinutes = ChronoUnit.MINUTES.between(turn.startTime(), turn.endTime());
            long breakMinutes = 0;

            if (turn.breakStart() != null && turn.breakEnd() != null) {
                breakMinutes = ChronoUnit.MINUTES.between(turn.breakStart(), turn.breakEnd());
            }

            long usableMinutes = totalMinutes - breakMinutes;
            long requiredPerSlot = dto.serviceDurationMinutes() + dto.intervalBetweenServices();

            if (usableMinutes < requiredPerSlot) {
                throw new MyException("El turno de " + turn.day() + " no permite ni un solo servicio. Ajusta duración o intervalo.");
            }
        }
    }
}

