package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.PlanDiscountRuleDTO;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;

@Component
public class ValidatePlanDiscountRuleCategoryPresence implements ValidationPlanDiscountRule{

    @Override
    public void validate(PlanDiscountRuleDTO rule) throws MyException {
        if (rule.category() == null) {
            throw new MyException("La categoría del cliente no puede estar vacía.");
        }
    }
}
