package com.petcare.back.service;

import com.petcare.back.domain.entity.PlanDiscountRule;
import com.petcare.back.repository.PlanDiscountRuleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PlanDiscountRuleService {

    private final PlanDiscountRuleRepository repository;

    public PlanDiscountRule createRule(PlanDiscountRule rule) {
        return repository.save(rule);
    }

    public List<PlanDiscountRule> getAllRules() {
        return repository.findAll();
    }

    public void deleteRule(Long id) {
        repository.deleteById(id);
    }
}

