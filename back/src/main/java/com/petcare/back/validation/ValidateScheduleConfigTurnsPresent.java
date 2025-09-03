package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.ScheduleConfigCreateDTO;
import com.petcare.back.domain.dto.request.ScheduleTurnCreateDTO;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;

@Component
public class ValidateScheduleConfigTurnsPresent implements ValidationScheduleConfig {

    @Override
    public void validate(ScheduleConfigCreateDTO dto) throws MyException {
        if (dto.turns() == null || dto.turns().isEmpty()) {
            throw new MyException("Debes configurar al menos un turno.");
        }

        for (ScheduleTurnCreateDTO turn : dto.turns()) {
            if (turn.day() == null) {
                throw new MyException("Cada turno debe tener un día asignado.");
            }
        }
    }
}
