package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.ScheduleConfigCreateDTO;
import com.petcare.back.domain.dto.request.ScheduleTurnCreateDTO;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;

@Component
public class ValidateScheduleTurnTimeRange implements ValidationScheduleConfig {

    @Override
    public void validate(ScheduleConfigCreateDTO dto) throws MyException {
        for (ScheduleTurnCreateDTO turn : dto.turns()) {
            if (!turn.startTime().isBefore(turn.endTime())) {
                throw new MyException("En el turno de " + turn.day() + ", la hora de inicio debe ser anterior a la de fin.");
            }
        }
    }
}

