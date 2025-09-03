package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;

@Component
public class ValidateBookingSchedulesNotEmpty implements ValidationBooking {

    @Override
    public void validate(BookingCreateDTO data) throws MyException {
        if (data.scheduleIds() == null || data.scheduleIds().isEmpty()) {
            throw new MyException("Debés seleccionar al menos un horario para la reserva");
        }
    }
}

