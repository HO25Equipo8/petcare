package com.petcare.back.validation;

import com.petcare.back.domain.entity.Booking;
import com.petcare.back.domain.entity.ServiceSession;
import com.petcare.back.domain.entity.User;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ValidateSessionServicesUserOwnershipTest {

    private final ValidateSessionServicesUserOwnership validator = new ValidateSessionServicesUserOwnership();

    @Test
    void shouldThrowIfUserIsNotOwnerOrProfessional() {
        User user = new User(); // el que intenta acceder
        User other = new User(); // el dueño y profesional
        user.setId(1L);
        other.setId(2L);

        Booking booking = new Booking();
        booking.setOwner(other);
        booking.setProfessionals(List.of(other)); // user no está

        ServiceSession session = new ServiceSession();
        session.setBooking(booking);

        assertThatThrownBy(() -> validator.validate(user, session))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("no tiene permisos");
    }

    @Test
    void shouldPassIfUserIsOwner() {
        User user = new User(); // el dueño
        Booking booking = new Booking();
        booking.setOwner(user);
        booking.setProfessionals(List.of(new User())); // ✅ lista válida

        ServiceSession session = new ServiceSession();
        session.setBooking(booking);

        assertThatCode(() -> validator.validate(user, session)).doesNotThrowAnyException();
    }

    @Test
    void shouldPassIfUserIsProfessional() {
        User user = new User(); // el profesional
        Booking booking = new Booking();
        booking.setOwner(new User());
        booking.setProfessionals(List.of(user)); // ✅ está en la lista

        ServiceSession session = new ServiceSession();
        session.setBooking(booking);

        assertThatCode(() -> validator.validate(user, session)).doesNotThrowAnyException();
    }
}
