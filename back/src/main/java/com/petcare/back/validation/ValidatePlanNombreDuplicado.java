package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.PlanCreateDTO;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidatePlanNombreDuplicado implements ValidationPlanCreate {

    private final PlanRepository repository;

    @Override
    public void validate(PlanCreateDTO dto) throws MyException {
        String generatedName = "Plan " + dto.frequencyEnum().getLabel() + " " + dto.intervalEnum().getLabel();
        if (repository.existsByName(generatedName)) {
            throw new MyException("Ya existe un plan con esa combinación de frecuencia e intervalo.");
        }
    }
}
