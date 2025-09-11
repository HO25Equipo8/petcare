package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.PetUpdateDTO;
import com.petcare.back.domain.entity.Pet;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;

@Component
public class ValidatePetUpdateBasicFields implements ValidationPetUpdate{

    @Override
    public void validate(Pet existingPet, PetUpdateDTO updateData) throws MyException {

        // Validate name if provided
        if (updateData.name() != null) {
            String name = updateData.name().trim();
            if (name.length() < 2) {
                throw new MyException("El nombre de la mascota debe tener al menos 2 caracteres");
            }
            if (name.length() > 50) {
                throw new MyException("El nombre de la mascota no puede exceder 50 caracteres");
            }
        }

        // Validate weight if provided
        if (updateData.weight() != null) {
            double weight = updateData.weight();
            if (weight <= 0.1) {
                throw new MyException("El peso debe ser mayor a 0.1kg");
            }
            if (weight > 200.0) {
                throw new MyException("El peso no puede exceder 200kg");
            }
        }

        // Validate text fields length
        validateTextFieldLength(updateData.breed(), "raza", 100);
        validateTextFieldLength(updateData.color(), "color", 50);
        validateTextFieldLength(updateData.allergies(), "alergias", 500);
        validateTextFieldLength(updateData.medications(), "medicamentos", 500);
        validateTextFieldLength(updateData.specialNeeds(), "necesidades especiales", 500);

        // Validate emergency contact format if provided
        if (updateData.emergencyContact() != null && !updateData.emergencyContact().trim().isEmpty()) {
            String contact = updateData.emergencyContact().trim();
            if (!contact.matches("^[+]?[0-9]{10,15}$")) {
                throw new MyException("El contacto de emergencia debe ser un número válido (10-15 dígitos)");
            }
        }
    }

    private void validateTextFieldLength(String field, String fieldName, int maxLength) throws MyException {
        if (field != null && field.length() > maxLength) {
            throw new MyException(String.format("El campo %s no puede exceder %d caracteres", fieldName, maxLength));
        }
    }
}
