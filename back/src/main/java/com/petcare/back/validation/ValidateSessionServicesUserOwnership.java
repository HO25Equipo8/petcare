package com.petcare.back.validation;

import com.petcare.back.domain.entity.BookingServiceItem;
import com.petcare.back.domain.entity.ServiceSession;
import com.petcare.back.domain.entity.User;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class ValidateSessionServicesUserOwnership implements ValidationSessionServices {

    @Override
    public void validate(User user, ServiceSession session) {
        boolean isOwner = session.getBooking().getOwner().equals(user);

        boolean isProfessional = session.getBooking().getServiceItems().stream()
                .map(BookingServiceItem::getProfessional)
                .anyMatch(prof -> prof.equals(user));

        if (!isOwner && !isProfessional) {
            throw new ValidationException("El usuario no tiene permisos sobre esta sesión.");
        }
    }
}

