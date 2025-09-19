package com.petcare.back.validation;

import com.petcare.back.domain.entity.Booking;
import com.petcare.back.domain.entity.ServiceSession;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.BookingStatusEnum;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ValidateSessionServicesBookingConfirmedTest {

    private final ValidateSessionServicesBookingConfirmed validator = new ValidateSessionServicesBookingConfirmed();

    @Test
    void shouldThrowIfBookingIsNotConfirmed() {
        Booking booking = new Booking();
        booking.setStatus(BookingStatusEnum.PENDIENTE);

        ServiceSession session = new ServiceSession();
        session.setBooking(booking);

        assertThatThrownBy(() -> validator.validate(new User(), session))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("no está confirmada");
    }

    @Test
    void shouldPassIfBookingIsConfirmed() {
        Booking booking = new Booking();
        booking.setStatus(BookingStatusEnum.CONFIRMADO);

        ServiceSession session = new ServiceSession();
        session.setBooking(booking);

        assertThatCode(() -> validator.validate(new User(), session)).doesNotThrowAnyException();
    }
}
