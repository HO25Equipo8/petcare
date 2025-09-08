package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.PlanDiscountRuleDTO;
import com.petcare.back.domain.entity.PlanDiscountRule;
import com.petcare.back.domain.entity.User;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.PlanDiscountRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ValidatePlanDiscountRuleNoOverlapWithExistingRules implements ValidationPlanDiscountRule {

    private final PlanDiscountRuleRepository repository;

    @Override
    public void validate(PlanDiscountRuleDTO rule) throws MyException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        List<PlanDiscountRule> existing = repository.findAllByCategoryAndSitter(rule.category(), user);

        for (PlanDiscountRule r : existing) {
            boolean overlaps = rule.minSessionsPerWeek() <= r.getMaxSessionsPerWeek()
                    && rule.maxSessionsPerWeek() >= r.getMinSessionsPerWeek();

            if (overlaps) {
                throw new MyException("Ya existe una regla que se superpone con este rango de sesiones.");
            }
        }
    }
}
