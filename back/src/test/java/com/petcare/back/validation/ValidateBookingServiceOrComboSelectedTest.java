package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.domain.dto.request.BookingServiceItemCreateDTO;
import com.petcare.back.exception.MyException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidateBookingServiceOrComboSelectedTest {

    ValidateBookingServiceOrComboSelected validator = new ValidateBookingServiceOrComboSelected();

    @Test
    void shouldPassWhenServiceIsSelected() throws MyException {
        BookingServiceItemCreateDTO item = new BookingServiceItemCreateDTO(1L, 100L, 10L); // offeringId, scheduleId, professionalId
        BookingCreateDTO dto = new BookingCreateDTO(1L, null, List.of(item));

        assertDoesNotThrow(() -> validator.validate(dto));
    }

    @Test
    void shouldPassWhenComboIsSelected() throws MyException {
        BookingCreateDTO dto = new BookingCreateDTO(1L, 2L, List.of()); // comboOfferingId presente, sin ítems

        assertDoesNotThrow(() -> validator.validate(dto));
    }

    @Test
    void shouldThrowWhenNeitherIsSelected() {
        BookingCreateDTO dto = new BookingCreateDTO(1L, null, List.of()); // sin combo, sin ítems

        assertThrows(MyException.class, () -> validator.validate(dto));
    }
}

