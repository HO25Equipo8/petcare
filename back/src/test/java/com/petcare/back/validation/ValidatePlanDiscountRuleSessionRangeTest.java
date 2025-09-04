package com.petcare.back.validation;

import com.petcare.back.domain.entity.PlanDiscountRule;
import com.petcare.back.exception.MyException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatePlanDiscountRuleSessionRangeTest {

    private final ValidatePlanDiscountRuleSessionRange validator = new ValidatePlanDiscountRuleSessionRange();

    @Test
    void shouldThrowIfMinIsZero() {
        PlanDiscountRule rule = new PlanDiscountRule();
        rule.setMinSessionsPerWeek(0.0);
        rule.setMaxSessionsPerWeek(2.0);

        assertThrows(MyException.class, () -> validator.validate(rule));
    }

    @Test
    void shouldThrowIfMaxIsLessThanMin() {
        PlanDiscountRule rule = new PlanDiscountRule();
        rule.setMinSessionsPerWeek(3.0);
        rule.setMaxSessionsPerWeek(2.0);

        assertThrows(MyException.class, () -> validator.validate(rule));
    }

    @Test
    void shouldPassIfRangeIsValid() {
        PlanDiscountRule rule = new PlanDiscountRule();
        rule.setMinSessionsPerWeek(1.0);
        rule.setMaxSessionsPerWeek(3.0);

        assertDoesNotThrow(() -> validator.validate(rule));
    }
}
