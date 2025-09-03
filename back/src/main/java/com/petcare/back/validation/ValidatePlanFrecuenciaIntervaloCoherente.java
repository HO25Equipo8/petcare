package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.PlanCreateDTO;
import com.petcare.back.domain.enumerated.IntervalEnum;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;

@Component
public class ValidatePlanFrecuenciaIntervaloCoherente implements ValidationPlanCreate {
    @Override
    public void validate(PlanCreateDTO dto) throws MyException {
        if (dto.frequencyEnum().getFrequencyPerWeek() > 7 && dto.intervalEnum() != IntervalEnum.SEMANAL) {
            throw new MyException("La frecuencia seleccionada no es coherente con el intervalo elegido.");
        }
    }
}

