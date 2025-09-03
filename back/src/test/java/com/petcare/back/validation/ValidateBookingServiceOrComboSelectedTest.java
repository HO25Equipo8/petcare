package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.exception.MyException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidateBookingServiceOrComboSelectedTest {

    ValidateBookingServiceOrComboSelected validator = new ValidateBookingServiceOrComboSelected();

    @Test
    void shouldPassWhenServiceIsSelected() throws MyException {
        BookingCreateDTO dto = new BookingCreateDTO(1L, 1L, null, null, List.of(1L), List.of());
        assertDoesNotThrow(() -> validator.validate(dto));
    }

    @Test
    void shouldPassWhenComboIsSelected() throws MyException {
        BookingCreateDTO dto = new BookingCreateDTO(1L, null, 2L, null, List.of(1L), List.of());
        assertDoesNotThrow(() -> validator.validate(dto));
    }

    @Test
    void shouldThrowWhenNeitherIsSelected() {
        BookingCreateDTO dto = new BookingCreateDTO(1L, null, null, null, List.of(1L), List.of());
        assertThrows(MyException.class, () -> validator.validate(dto));
    }
}
