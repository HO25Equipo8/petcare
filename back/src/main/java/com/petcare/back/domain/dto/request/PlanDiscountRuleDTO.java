package com.petcare.back.domain.dto.request;

import com.petcare.back.domain.enumerated.CustomerCategory;
import java.math.BigDecimal;

public record PlanDiscountRuleDTO(
        CustomerCategory category,
        Double minSessionsPerWeek,
        Double maxSessionsPerWeek,
        BigDecimal discount
) {
}
