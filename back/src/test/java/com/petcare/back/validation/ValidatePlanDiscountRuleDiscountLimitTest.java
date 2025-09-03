package com.petcare.back.validation;

import com.petcare.back.domain.entity.PlanDiscountRule;
import com.petcare.back.exception.MyException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatePlanDiscountRuleDiscountLimitTest {

    private final ValidatePlanDiscountRuleDiscountLimit validator = new ValidatePlanDiscountRuleDiscountLimit();

    @Test
    void shouldThrowIfDiscountIsNull() {
        PlanDiscountRule rule = new PlanDiscountRule();
        rule.setDiscount(null);

        assertThrows(MyException.class, () -> validator.validate(rule));
    }

    @Test
    void shouldThrowIfDiscountIsNegative() {
        PlanDiscountRule rule = new PlanDiscountRule();
        rule.setDiscount(BigDecimal.valueOf(-5));

        assertThrows(MyException.class, () -> validator.validate(rule));
    }

    @Test
    void shouldThrowIfDiscountIsTooHigh() {
        PlanDiscountRule rule = new PlanDiscountRule();
        rule.setDiscount(BigDecimal.valueOf(85));

        assertThrows(MyException.class, () -> validator.validate(rule));
    }

    @Test
    void shouldPassIfDiscountIsValid() {
        PlanDiscountRule rule = new PlanDiscountRule();
        rule.setDiscount(BigDecimal.valueOf(20));

        assertDoesNotThrow(() -> validator.validate(rule));
    }
}
