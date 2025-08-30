package com.petcare.back.domain.dto.request;

import com.petcare.back.domain.enumerated.OfferingEnum;
import com.petcare.back.domain.enumerated.PetTypeEnum;
import com.petcare.back.domain.enumerated.ProfessionalRoleEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record OfferingCreateDTO(
        @NotNull OfferingEnum name,
        @NotBlank String description,
        @NotNull @DecimalMin("0.00") BigDecimal basePrice,
        @NotNull(message = "Debe especificarse al menos un tipo de mascota")
        List<PetTypeEnum> applicablePetTypes,
        @NotNull(message = "Debe especificarse un rol profesional válido")
        ProfessionalRoleEnum allowedRole) {
}
