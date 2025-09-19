package com.petcare.back.domain.dto.response;

import com.petcare.back.domain.enumerated.CustomerCategory;
import java.math.BigDecimal;

public record PlanDiscountRuleResponseDTO(
        Long id,
        CustomerCategory category,
        Double minSessionsPerWeek,
        Double maxSessionsPerWeek,
        BigDecimal discount
) {
}
