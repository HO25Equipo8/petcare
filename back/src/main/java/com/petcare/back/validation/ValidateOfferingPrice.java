package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.OfferingCreateDTO;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ValidateOfferingPrice implements ValidationOffering {

    private static final BigDecimal MAX_PRICE = new BigDecimal("100000.00");

    @Override
    public void validar(OfferingCreateDTO dto) throws MyException {
        BigDecimal price = dto.basePrice();

        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new MyException("El precio base debe ser mayor a cero.");
        }

        if (price.compareTo(MAX_PRICE) > 0) {
            throw new MyException("El precio base no puede superar los " + MAX_PRICE + " pesos.");
        }
    }
}
