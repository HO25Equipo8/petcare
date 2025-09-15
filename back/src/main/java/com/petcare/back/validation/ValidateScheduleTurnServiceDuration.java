package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.ScheduleConfigCreateDTO;
import com.petcare.back.domain.dto.request.ScheduleTurnCreateDTO;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@Component
public class ValidateScheduleTurnServiceDuration implements ValidationScheduleConfig {

    @Override
    public void validate(ScheduleConfigCreateDTO dto) throws MyException {
        if (dto.serviceDurationMinutes() <= 0) {
            throw new MyException("La duración del servicio debe ser mayor a 0.");
        }

        for (ScheduleTurnCreateDTO turn : dto.turns()) {
            long turnMinutes = ChronoUnit.MINUTES.between(turn.startTime(), turn.endTime());
            if (dto.serviceDurationMinutes() > turnMinutes) {
                throw new MyException("La duración del servicio excede el turno de " + turn.day() + ".");
            }
        }
    }
}


