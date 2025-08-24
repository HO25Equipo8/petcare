package com.petcare.back.domain.dto.request;

import com.petcare.back.domain.enumerated.PetTypeEnum;
import com.petcare.back.domain.enumerated.ServicePetsEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record ServiceCreateDTO(
        @NotNull ServicePetsEnum name,
        @NotBlank String description,
        @NotNull @DecimalMin("0.00") BigDecimal basePrice,
        List<PetTypeEnum> applicablePetTypes) {
}
