package com.petcare.back.domain.dto.response;

import com.petcare.back.domain.enumerated.*;

public record PetResponseDTO(
        Long id,
        String name,
        PetTypeEnum type,
        String breed,
        Integer age, // calculada
        Double weight,
        String color,
        PetSizeEnum petSize,
        String microchip,
        TemperamentEnum temperament,
        VaccinationEnum vaccination,
        HealthStatusEnum health,
        String allergies,
        String medications,
        String specialNeeds,
        String emergencyContact,
        Boolean active,
        Long ownerId,
        String ownerEmail,
        Long imageId
) {}
