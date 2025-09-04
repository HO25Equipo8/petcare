package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;

@Component
public class ValidateBookingServiceOrComboSelected implements ValidationBooking {

    @Override
    public void validate(BookingCreateDTO data) throws MyException {

        boolean sinServicio = data.offeringId() == null || data.offeringId() == 0;
        boolean sinCombo = data.comboOfferingId() == null || data.comboOfferingId() == 0;

        if (sinServicio && sinCombo) {
            throw new MyException("Debés seleccionar al menos un servicio o un combo para realizar la reserva");
        }
    }
}
