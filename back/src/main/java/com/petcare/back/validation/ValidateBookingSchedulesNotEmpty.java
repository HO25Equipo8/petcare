package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;

@Component
public class ValidateBookingSchedulesNotEmpty implements ValidationBooking {

    @Override
    public void validate(BookingCreateDTO data) throws MyException {
        if (data.items() == null || data.items().isEmpty()) {
            throw new MyException("Debés agregar al menos un servicio a la reserva");
        }

        boolean algunoSinHorario = data.items().stream()
                .anyMatch(item -> item.scheduleId() == null);

        if (algunoSinHorario) {
            throw new MyException("Todos los servicios deben tener un horario asignado");
        }
    }
}

