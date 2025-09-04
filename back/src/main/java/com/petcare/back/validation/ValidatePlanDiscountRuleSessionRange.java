package com.petcare.back.validation;

import com.petcare.back.domain.entity.PlanDiscountRule;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;

@Component
public class ValidatePlanDiscountRuleSessionRange implements ValidationPlanDiscountRule{

    @Override
    public void validate(PlanDiscountRule rule) throws MyException {
        if (rule.getMinSessionsPerWeek() <= 0) {
            throw new MyException("El mínimo de sesiones por semana debe ser mayor a 0.");
        }
        if (rule.getMaxSessionsPerWeek() < rule.getMinSessionsPerWeek()) {
            throw new MyException("El máximo de sesiones debe ser mayor o igual al mínimo.");
        }
    }
}
