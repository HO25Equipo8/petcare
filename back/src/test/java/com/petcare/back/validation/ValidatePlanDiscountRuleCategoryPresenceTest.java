package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.PlanDiscountRuleDTO;
import com.petcare.back.domain.entity.PlanDiscountRule;
import com.petcare.back.domain.enumerated.CustomerCategory;
import com.petcare.back.exception.MyException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatePlanDiscountRuleCategoryPresenceTest {

    private final ValidatePlanDiscountRuleCategoryPresence validator = new ValidatePlanDiscountRuleCategoryPresence();

    @Test
    void shouldThrowIfCategoryIsNull() {
        PlanDiscountRuleDTO rule = new PlanDiscountRuleDTO(
                null,
                2.0,
                4.0,
                new BigDecimal("10.0")
        );

        assertThrows(MyException.class, () -> validator.validate(rule));
    }

    @Test
    void shouldPassIfCategoryIsPresent() {
        PlanDiscountRuleDTO rule = new PlanDiscountRuleDTO(
                CustomerCategory.FRECUENTE,
                2.0,
                4.0,
                new BigDecimal("10.0")
        );

        assertDoesNotThrow(() -> validator.validate(rule));
    }
}
