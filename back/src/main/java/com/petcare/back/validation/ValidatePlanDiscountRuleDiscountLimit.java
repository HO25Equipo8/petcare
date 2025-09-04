package com.petcare.back.validation;

import com.petcare.back.domain.entity.PlanDiscountRule;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ValidatePlanDiscountRuleDiscountLimit implements ValidationPlanDiscountRule{

    @Override
    public void validate(PlanDiscountRule rule) throws MyException {
        BigDecimal discount = rule.getDiscount();
        if (discount == null || discount.compareTo(BigDecimal.ZERO) < 0 || discount.compareTo(BigDecimal.valueOf(80)) > 0) {
            throw new MyException("El descuento debe estar entre 0% y 80%.");
        }
    }
}
