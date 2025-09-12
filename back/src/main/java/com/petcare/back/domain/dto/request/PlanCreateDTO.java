package com.petcare.back.domain.dto.request;

import com.petcare.back.domain.enumerated.PlanType;
import jakarta.validation.constraints.NotNull;

public record PlanCreateDTO(
        @NotNull PlanType type,
        @NotNull Double price,
        String description,
        boolean trackingEnabled,
        boolean liveUpdatesEnabled
) {}
