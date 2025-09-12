package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.PlanCreateDTO;
import com.petcare.back.domain.enumerated.PlanType;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;

@Component
public class ValidatePlanFuncionalidadActiva implements ValidationPlanCreate {

    @Override
    public void validate(PlanCreateDTO dto) throws MyException {
        if (dto.type() == PlanType.BASE) {
            return; // El plan BASE puede no tener funcionalidades activas
        }

        if (!dto.trackingEnabled() && !dto.liveUpdatesEnabled()) {
            throw new MyException("El plan debe habilitar al menos una funcionalidad: tracking o actualizaciones en vivo.");
        }
    }
}

