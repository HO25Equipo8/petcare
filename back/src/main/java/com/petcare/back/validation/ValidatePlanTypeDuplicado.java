package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.PlanCreateDTO;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidatePlanTypeDuplicado implements ValidationPlanCreate {

    private final PlanRepository repository;

    @Override
    public void validate(PlanCreateDTO dto) throws MyException {
        boolean exists = repository.existsByType(dto.type());
        if (exists) {
            throw new MyException("Ya existe un plan con el tipo " + dto.type().name());
        }
    }
}
