package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.PetUpdateDTO;
import com.petcare.back.domain.entity.Pet;
import com.petcare.back.domain.enumerated.PetSizeEnum;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;

@Component
public class ValidatePetUpdateWeightSize implements ValidationPetUpdate {

    @Override
    public void validate(Pet existingPet, PetUpdateDTO updateData) throws MyException {
        // Get the size to validate against (either from update or existing)
        PetSizeEnum sizeToCheck = updateData.petSize() != null ?
                updateData.petSize() : existingPet.getPetSize();

        // Get the weight to validate (either from update or existing)
        Double weightToCheck = updateData.weight() != null ?
                updateData.weight() : existingPet.getWeight();

        // Only validate if both size and weight are available
        if (sizeToCheck == null || weightToCheck == null) {
            return;
        }

        double weight = weightToCheck;
        boolean isValid = switch (sizeToCheck) {
            case SMALL -> weight >= 0.5 && weight <= 10.0;
            case MEDIUM -> weight >= 5.0 && weight <= 25.0;
            case LARGE -> weight >= 15.0 && weight <= 50.0;
            case EXTRA_LARGE -> weight >= 30.0 && weight <= 100.0;
        };

        if (!isValid) {
            String ranges = switch (sizeToCheck) {
                case SMALL -> "0.5kg - 10kg";
                case MEDIUM -> "5kg - 25kg";
                case LARGE -> "15kg - 50kg";
                case EXTRA_LARGE -> "30kg - 100kg";
            };
            throw new MyException(String.format(
                    "El peso %.1fkg no es realista para una mascota de tamaño %s (rango esperado: %s)",
                    weight, sizeToCheck, ranges));
        }
    }
}
