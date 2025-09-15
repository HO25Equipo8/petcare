package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;

@Component
public class ValidateBookingServiceOrComboSelected implements ValidationBooking {

    @Override
    public void validate(BookingCreateDTO data) throws MyException {
        boolean sinCombo = data.comboOfferingId() == null || data.comboOfferingId() == 0;

        boolean sinServicios = data.items() == null || data.items().isEmpty() ||
                data.items().stream().allMatch(item -> item.offeringId() == null || item.offeringId() == 0);

        if (sinCombo && sinServicios) {
            throw new MyException("Debés seleccionar al menos un servicio o un combo para realizar la reserva");
        }
    }
}
