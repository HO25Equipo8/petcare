package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.OfferingCreateDTO;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;

@Component
public class ValidateOfferingPetTypes implements ValidationOffering {

    @Override
    public void validate(OfferingCreateDTO dto) throws MyException {
        if (dto.applicablePetTypes() == null || dto.applicablePetTypes().isEmpty()) {
            throw new MyException("Debe especificarse al menos un tipo de mascota aplicable.");
        }
    }
}
