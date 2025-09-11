package com.petcare.back.validation;

import com.petcare.back.domain.entity.*;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ValidateSessionServicesWithinBookingTimeTest {

    private final ValidateSessionServicesWithinBookingTime validator = new ValidateSessionServicesWithinBookingTime();

    @Test
    void shouldThrowIfNowIsOutsideBookingRange() {
        Booking booking = mock(Booking.class);
        Schedule schedule = mock(Schedule.class);
        ScheduleConfig config = mock(ScheduleConfig.class);

        when(schedule.getEstablishedTime()).thenReturn(Instant.now().minus(2, ChronoUnit.HOURS));
        when(config.getServiceDurationMinutes()).thenReturn(30);
        when(schedule.getScheduleConfig()).thenReturn(config);
        when(booking.getSchedules()).thenReturn(List.of(schedule));

        ServiceSession session = new ServiceSession();
        session.setBooking(booking);

        assertThatThrownBy(() -> validator.validate(new User(), session))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("fuera del horario");
    }

    @Test
    void shouldPassIfNowIsWithinBookingRange() {
        Booking booking = mock(Booking.class);
        Schedule schedule = mock(Schedule.class);
        ScheduleConfig config = mock(ScheduleConfig.class);

        when(schedule.getEstablishedTime()).thenReturn(Instant.now());
        when(config.getServiceDurationMinutes()).thenReturn(30);
        when(schedule.getScheduleConfig()).thenReturn(config);
        when(booking.getSchedules()).thenReturn(List.of(schedule));

        ServiceSession session = new ServiceSession();
        session.setBooking(booking);

        assertThatCode(() -> validator.validate(new User(), session)).doesNotThrowAnyException();
    }
}
