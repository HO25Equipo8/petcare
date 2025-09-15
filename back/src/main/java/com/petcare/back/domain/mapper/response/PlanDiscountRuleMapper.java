package com.petcare.back.domain.mapper.response;

import com.petcare.back.domain.dto.response.PlanDiscountRuleResponseDTO;
import com.petcare.back.domain.entity.PlanDiscountRule;
import org.springframework.stereotype.Component;

@Component
public class PlanDiscountRuleMapper {
    public PlanDiscountRuleResponseDTO toDto(PlanDiscountRule rule) {
        return new PlanDiscountRuleResponseDTO(
                rule.getId(),
                rule.getCategory(),
                rule.getMinSessionsPerWeek(),
                rule.getMaxSessionsPerWeek(),
                rule.getDiscount()
        );
    }
}
