package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.OfferingCreateDTO;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;

@Component
public class ValidateOfferingName implements ValidationOffering {

    @Override
    public void validar(OfferingCreateDTO dto) throws MyException {
        if (dto.name() == null) {
            throw new MyException("El nombre del servicio es obligatorio.");
        }
    }
}
