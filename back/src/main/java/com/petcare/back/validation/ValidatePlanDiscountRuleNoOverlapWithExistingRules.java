package com.petcare.back.validation;

import com.petcare.back.domain.entity.PlanDiscountRule;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.PlanDiscountRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ValidatePlanDiscountRuleNoOverlapWithExistingRules implements ValidationPlanDiscountRule{

    private final PlanDiscountRuleRepository repository;

    @Override
    public void validate(PlanDiscountRule rule) throws MyException {
        List<PlanDiscountRule> existing = repository.findAllByCategory(rule.getCategory());

        for (PlanDiscountRule r : existing) {
            boolean overlaps = rule.getMinSessionsPerWeek() <= r.getMaxSessionsPerWeek()
                    && rule.getMaxSessionsPerWeek() >= r.getMinSessionsPerWeek();

            if (overlaps && !r.getId().equals(rule.getId())) {
                throw new MyException("Ya existe una regla que se superpone con este rango de sesiones.");
            }
        }
    }
}
