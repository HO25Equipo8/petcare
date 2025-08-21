package com.petcare.back.domain.dto.request;

import com.petcare.back.domain.enumerated.HealthStatusEnum;
import com.petcare.back.domain.enumerated.PetTypeEnum;
import com.petcare.back.domain.enumerated.TemperamentEnum;
import com.petcare.back.domain.enumerated.VaccinationEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PetCreateDTO(
        @NotBlank
        String name,
        @NotNull
        PetTypeEnum type,
        String breed,
        @Min(0)
        Integer age,
        TemperamentEnum temperament,
        VaccinationEnum vaccination,
        HealthStatusEnum health
) {
}
