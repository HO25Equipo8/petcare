package com.petcare.back.validation;

import com.petcare.back.domain.entity.PlanDiscountRule;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.CustomerCategory;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.PlanDiscountRuleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidatePlanDiscountRuleNoOverlapWithExistingRulesTest {

    @Mock
    private PlanDiscountRuleRepository repository;

    @InjectMocks
    private ValidatePlanDiscountRuleNoOverlapWithExistingRules validator;


    @Test
    void shouldThrowIfOverlapExists() {
        User mockSitter = new User();
        mockSitter.setId(99L);
        mockSitter.setRole(Role.SITTER);

        PlanDiscountRule existing = new PlanDiscountRule(
                1L,
                CustomerCategory.FRECUENTE,
                1.0,
                3.0,
                BigDecimal.TEN,
                mockSitter
        );

        when(repository.findAllByCategory(CustomerCategory.FRECUENTE)).thenReturn(List.of(existing));

        PlanDiscountRule newRule = new PlanDiscountRule(
                null,
                CustomerCategory.FRECUENTE,
                2.0,
                4.0,
                BigDecimal.TEN,
                mockSitter
        );

        assertThrows(MyException.class, () -> validator.validate(newRule));
    }

    @Test
    void shouldPassIfNoOverlap() {
        User mockSitter = new User();
        mockSitter.setId(99L);
        mockSitter.setRole(Role.SITTER);

        PlanDiscountRule existing = new PlanDiscountRule(
                1L,
                CustomerCategory.FRECUENTE,
                1.0,
                2.0,
                BigDecimal.TEN,
                mockSitter
        );

        when(repository.findAllByCategory(CustomerCategory.FRECUENTE)).thenReturn(List.of(existing));

        PlanDiscountRule newRule = new PlanDiscountRule(
                null,
                CustomerCategory.FRECUENTE,
                3.0,
                4.0,
                BigDecimal.TEN,
                mockSitter
        );

        assertDoesNotThrow(() -> validator.validate(newRule));
    }
}