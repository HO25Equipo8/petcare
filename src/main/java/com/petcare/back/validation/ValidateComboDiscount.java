package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.ComboOfferingCreateDTO;
import com.petcare.back.domain.entity.Offering;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValidateComboDiscount implements ValidationCombo {

    @Override
    public void validate(ComboOfferingCreateDTO dto, List<Offering> offerings) throws MyException {
        double discount = dto.discount();
        if (discount < 0 || discount > 100) {
            throw new MyException("El descuento debe estar entre 0% y 100%.");
        }
    }
}