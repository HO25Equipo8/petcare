package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.PetUpdateDTO;
import com.petcare.back.domain.entity.Pet;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ValidatePetUpdateBirthDate implements ValidationPetUpdate {

    @Override
    public void validate(Pet existingPet, PetUpdateDTO updateData) throws MyException {
        if (updateData.birthDate() == null) {
            return; // Null is allowed for updates - field won't change
        }

        LocalDate birthDate = updateData.birthDate();
        LocalDate now = LocalDate.now();

        // Can't be in the future
        if (birthDate.isAfter(now)) {
            throw new MyException("La fecha de nacimiento no puede ser futura");
        }

        // Reasonable age limits (max 30 years for most pets)
        if (birthDate.isBefore(now.minusYears(30))) {
            throw new MyException("La fecha de nacimiento es demasiado antigua (máximo 30 años)");
        }

        // Minimum age (at least 1 day old)
        if (birthDate.isAfter(now.minusDays(1))) {
            throw new MyException("La mascota debe tener al menos 1 día de edad");
        }
    }
}
