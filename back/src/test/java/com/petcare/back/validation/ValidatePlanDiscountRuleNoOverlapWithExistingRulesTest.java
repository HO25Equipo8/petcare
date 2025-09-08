package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.PlanDiscountRuleDTO;
import com.petcare.back.domain.entity.PlanDiscountRule;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.CustomerCategory;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.PlanDiscountRuleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

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

    @BeforeEach
    void setup() {
        User mockSitter = new User();
        mockSitter.setId(99L);
        mockSitter.setRole(Role.SITTER);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(mockSitter, null)
        );
    }

    @Test
    void shouldThrowIfOverlapExists() {
        User sitter = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        PlanDiscountRule existing = new PlanDiscountRule(
                1L,
                CustomerCategory.FRECUENTE,
                1.0,
                3.0,
                BigDecimal.TEN,
                sitter
        );

        when(repository.findAllByCategoryAndSitter(CustomerCategory.FRECUENTE, sitter))
                .thenReturn(List.of(existing));

        PlanDiscountRuleDTO newRule = new PlanDiscountRuleDTO(
                CustomerCategory.FRECUENTE,
                2.0,
                4.0,
                BigDecimal.TEN
        );

        assertThrows(MyException.class, () -> validator.validate(newRule));
    }

    @Test
    void shouldPassIfNoOverlap() {
        User sitter = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        PlanDiscountRule existing = new PlanDiscountRule(
                1L,
                CustomerCategory.FRECUENTE,
                1.0,
                2.0,
                BigDecimal.TEN,
                sitter
        );

        when(repository.findAllByCategoryAndSitter(CustomerCategory.FRECUENTE, sitter))
                .thenReturn(List.of(existing));

        PlanDiscountRuleDTO newRule = new PlanDiscountRuleDTO(
                CustomerCategory.FRECUENTE,
                3.0,
                4.0,
                BigDecimal.TEN
        );

        assertDoesNotThrow(() -> validator.validate(newRule));
    }
}
