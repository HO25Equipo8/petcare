package com.petcare.back.domain.dto.response;

import com.petcare.back.domain.enumerated.PlanType;

public record PlanResponseDTO(
        Long id,
        PlanType type,
        Double price,
        String description,
        boolean trackingEnabled,
        boolean liveUpdatesEnabled
) {}
