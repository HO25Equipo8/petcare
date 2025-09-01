package com.petcare.back.service;

import com.petcare.back.domain.entity.PlanDiscountRule;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.PlanDiscountRuleRepository;
import com.petcare.back.validation.ValidationPlanDiscountRule;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PlanDiscountRuleService {

    private final PlanDiscountRuleRepository repository;
    private final List<ValidationPlanDiscountRule> validationPlanDiscountRules;

    public PlanDiscountRule createRule(PlanDiscountRule rule) throws MyException {

        for (ValidationPlanDiscountRule v : validationPlanDiscountRules) {
            v.validate(rule);
        }
        return repository.save(rule);
    }

    public List<PlanDiscountRule> getAllRules() {
        return repository.findAll();
    }

    public void deleteRule(Long id) {
        repository.deleteById(id);
    }
}

