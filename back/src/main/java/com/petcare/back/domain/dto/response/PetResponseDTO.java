package com.petcare.back.domain.dto.response;

import com.petcare.back.domain.enumerated.HealthStatusEnum;
import com.petcare.back.domain.enumerated.PetTypeEnum;
import com.petcare.back.domain.enumerated.TemperamentEnum;
import com.petcare.back.domain.enumerated.VaccinationEnum;

public record PetResponseDTO(
        Long id,
        String name,
        PetTypeEnum type,
        String breed,
        Integer age,
        TemperamentEnum temperament,
        VaccinationEnum vaccination,
        HealthStatusEnum health,
        Long ownerId
) {}
