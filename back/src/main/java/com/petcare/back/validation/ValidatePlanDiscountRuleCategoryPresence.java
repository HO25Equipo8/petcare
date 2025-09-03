package com.petcare.back.validation;

import com.petcare.back.domain.entity.PlanDiscountRule;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;

@Component
public class ValidatePlanDiscountRuleCategoryPresence implements ValidationPlanDiscountRule{

    @Override
    public void validate(PlanDiscountRule rule) throws MyException {
        if (rule.getCategory() == null) {
            throw new MyException("La categoría del cliente no puede estar vacía.");
        }
    }
}
