package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.ScheduleConfigCreateDTO;
import com.petcare.back.domain.dto.request.ScheduleTurnCreateDTO;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;

@Component
public class ValidateScheduleTurnBreakWithinShift implements ValidationScheduleConfig {

    @Override
    public void validate(ScheduleConfigCreateDTO dto) throws MyException {
        for (ScheduleTurnCreateDTO turn : dto.turns()) {
            if (turn.breakStart() != null && turn.breakEnd() != null) {
                if (!turn.breakStart().isBefore(turn.breakEnd())) {
                    throw new MyException("La pausa en " + turn.day() + " debe tener una hora de inicio anterior a la de fin.");
                }
                if (turn.breakStart().isBefore(turn.startTime()) || turn.breakEnd().isAfter(turn.endTime())) {
                    throw new MyException("La pausa en " + turn.day() + " debe estar dentro del rango horario del turno.");
                }
            }
        }
    }
}

