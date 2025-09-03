package com.petcare.back.validation;

import com.petcare.back.domain.entity.PlanDiscountRule;
import com.petcare.back.domain.enumerated.CustomerCategory;
import com.petcare.back.exception.MyException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatePlanDiscountRuleCategoryPresenceTest {

    private final ValidatePlanDiscountRuleCategoryPresence validator = new ValidatePlanDiscountRuleCategoryPresence();

    @Test
    void shouldThrowIfCategoryIsNull() {
        PlanDiscountRule rule = new PlanDiscountRule();
        rule.setCategory(null);

        assertThrows(MyException.class, () -> validator.validate(rule));
    }

    @Test
    void shouldPassIfCategoryIsPresent() {
        PlanDiscountRule rule = new PlanDiscountRule();
        rule.setCategory(CustomerCategory.FRECUENTE);

        assertDoesNotThrow(() -> validator.validate(rule));
    }
}

