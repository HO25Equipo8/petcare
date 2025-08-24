package com.petcare.back.domain.dto.request;

import com.petcare.back.domain.enumerated.ComboEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ComboServiceCreateDTO(
        @NotNull ComboEnum name,
        @NotBlank String description,
        @NotNull @DecimalMin("0.0") Double discount,
        @NotEmpty List<Long> serviceIds
) {
}
