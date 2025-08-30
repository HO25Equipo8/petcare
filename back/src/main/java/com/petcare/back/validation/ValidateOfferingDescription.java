package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.OfferingCreateDTO;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;

@Component
public class ValidateOfferingDescription implements ValidationOffering {

    @Override
    public void validar(OfferingCreateDTO dto) throws MyException {
        if (dto.description() == null || dto.description().trim().isEmpty()) {
            throw new MyException("La descripción del servicio no puede estar vacía.");
        }
    }
}
