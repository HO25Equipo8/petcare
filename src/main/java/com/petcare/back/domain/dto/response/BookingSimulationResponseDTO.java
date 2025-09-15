package com.petcare.back.domain.dto.response;

import java.math.BigDecimal;

public record BookingSimulationResponseDTO(
        BigDecimal basePrice,
        BigDecimal comboDiscountAmount,
        BigDecimal planDiscountAmount,
        BigDecimal finalPrice,
        boolean isViable,
        String recommendation
) {
}
