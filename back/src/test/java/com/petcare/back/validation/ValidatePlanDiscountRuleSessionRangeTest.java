package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.PlanDiscountRuleDTO;
import com.petcare.back.domain.entity.PlanDiscountRule;
import com.petcare.back.domain.enumerated.CustomerCategory;
import com.petcare.back.exception.MyException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatePlanDiscountRuleSessionRangeTest {

    private final ValidatePlanDiscountRuleSessionRange validator = new ValidatePlanDiscountRuleSessionRange();

    @Test
    void shouldThrowIfMinIsZero() {
        PlanDiscountRuleDTO rule = new PlanDiscountRuleDTO(
                CustomerCategory.FRECUENTE,
                0.0,
                2.0,
                BigDecimal.TEN
        );

        assertThrows(MyException.class, () -> validator.validate(rule));
    }

    @Test
    void shouldThrowIfMaxIsLessThanMin() {
        PlanDiscountRuleDTO rule = new PlanDiscountRuleDTO(
                CustomerCategory.FRECUENTE,
                3.0,
                2.0,
                BigDecimal.TEN
        );

        assertThrows(MyException.class, () -> validator.validate(rule));
    }

    @Test
    void shouldPassIfRangeIsValid() {
        PlanDiscountRuleDTO rule = new PlanDiscountRuleDTO(
                CustomerCategory.FRECUENTE,
                1.0,
                3.0,
                BigDecimal.TEN
        );

        assertDoesNotThrow(() -> validator.validate(rule));
    }
}
