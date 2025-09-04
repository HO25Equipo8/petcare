package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.ComboOfferingRepository;
import org.springframework.stereotype.Component;

@Component
public class ValidateBookingComboExists implements ValidationBooking {

    private final ComboOfferingRepository comboRepository;

    public ValidateBookingComboExists(ComboOfferingRepository comboRepository) {
        this.comboRepository = comboRepository;
    }

    @Override
    public void validate(BookingCreateDTO data) throws MyException {
        if (data.comboOfferingId() != null &&
                !comboRepository.existsById(data.comboOfferingId())) {
            throw new MyException("El combo seleccionado no existe");
        }
    }
}
