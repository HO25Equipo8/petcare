package com.petcare.back.domain.dto.request;

import com.petcare.back.domain.enumerated.ComboEnum;
import jakarta.validation.constraints.DecimalMin;

import java.util.List;

public record ComboOfferingUpdateDTO(
        ComboEnum name,
        String description,
        @DecimalMin("0.0") Double discount,
        List<Long> offeringIds
) {
}
