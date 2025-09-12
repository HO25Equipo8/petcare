package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.PlanCreateDTO;
import com.petcare.back.domain.enumerated.PlanType;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;

@Component
public class ValidatePlanPrecioPositivo implements ValidationPlanCreate {

    @Override
    public void validate(PlanCreateDTO dto) throws MyException {
        if (dto.type() == PlanType.BASE) {
            return; // El plan BASE puede tener precio cero
        }

        if (dto.price() == null || dto.price() <= 0) {
            throw new MyException("El precio del plan debe ser mayor a cero.");
        }
    }
}
