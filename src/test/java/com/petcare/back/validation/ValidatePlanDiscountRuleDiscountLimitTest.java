package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.PlanDiscountRuleDTO;
import com.petcare.back.domain.entity.PlanDiscountRule;
import com.petcare.back.domain.enumerated.CustomerCategory;
import com.petcare.back.exception.MyException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatePlanDiscountRuleDiscountLimitTest {

    private final ValidatePlanDiscountRuleDiscountLimit validator = new ValidatePlanDiscountRuleDiscountLimit();

    @Test
    void shouldThrowIfDiscountIsNull() {
        PlanDiscountRuleDTO rule = new PlanDiscountRuleDTO(
                CustomerCategory.FRECUENTE,
                2.0,
                4.0,
                null
        );

        assertThrows(MyException.class, () -> validator.validate(rule));
    }

    @Test
    void shouldThrowIfDiscountIsNegative() {
        PlanDiscountRuleDTO rule = new PlanDiscountRuleDTO(
                CustomerCategory.FRECUENTE,
                2.0,
                4.0,
                BigDecimal.valueOf(-5)
        );

        assertThrows(MyException.class, () -> validator.validate(rule));
    }

    @Test
    void shouldThrowIfDiscountIsTooHigh() {
        PlanDiscountRuleDTO rule = new PlanDiscountRuleDTO(
                CustomerCategory.FRECUENTE,
                2.0,
                4.0,
                BigDecimal.valueOf(85)
        );

        assertThrows(MyException.class, () -> validator.validate(rule));
    }

    @Test
    void shouldPassIfDiscountIsValid() {
        PlanDiscountRuleDTO rule = new PlanDiscountRuleDTO(
                CustomerCategory.FRECUENTE,
                2.0,
                4.0,
                BigDecimal.valueOf(20)
        );

        assertDoesNotThrow(() -> validator.validate(rule));
    }
}
