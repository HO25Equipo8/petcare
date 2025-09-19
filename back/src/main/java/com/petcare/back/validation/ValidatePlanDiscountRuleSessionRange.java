package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.PlanDiscountRuleDTO;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;

@Component
public class ValidatePlanDiscountRuleSessionRange implements ValidationPlanDiscountRule{

    @Override
    public void validate(PlanDiscountRuleDTO rule) throws MyException {
        if (rule.minSessionsPerWeek() <= 0) {
            throw new MyException("El mínimo de sesiones por semana debe ser mayor a 0.");
        }
        if (rule.maxSessionsPerWeek() < rule.minSessionsPerWeek()) {
            throw new MyException("El máximo de sesiones debe ser mayor o igual al mínimo.");
        }
    }
}
