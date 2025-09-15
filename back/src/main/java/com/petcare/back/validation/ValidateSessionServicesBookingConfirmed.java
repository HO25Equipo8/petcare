package com.petcare.back.validation;

import com.petcare.back.domain.entity.ServiceSession;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.BookingStatusEnum;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class ValidateSessionServicesBookingConfirmed implements ValidationSessionServices {

    @Override
    public void validate(User user, ServiceSession session) {
        if (!session.getBooking().getStatus().equals(BookingStatusEnum.CONFIRMADO)) {
            throw new ValidationException("La sesión no puede iniciar porque la reserva no está confirmada.");
        }
    }
}
