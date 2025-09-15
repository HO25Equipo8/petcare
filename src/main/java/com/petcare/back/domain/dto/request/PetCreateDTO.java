package com.petcare.back.domain.dto.request;

import com.petcare.back.domain.enumerated.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PetCreateDTO(
        @NotBlank
        String name,
        @NotNull
        PetTypeEnum type,
        String breed,
        Double weight,
        String color,
        PetSizeEnum petSize,
        LocalDate birthDate,
        String microchip,
        TemperamentEnum temperament,
        VaccinationEnum vaccination,
        HealthStatusEnum health,
        String allergies,
        String medications,
        String specialNeeds,
        String emergencyContact
) {}
