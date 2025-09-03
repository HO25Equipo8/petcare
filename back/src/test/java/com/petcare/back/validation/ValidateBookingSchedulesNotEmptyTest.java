package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.exception.MyException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidateBookingSchedulesNotEmptyTest {

    ValidateBookingSchedulesNotEmpty validator = new ValidateBookingSchedulesNotEmpty();

    @Test
    void shouldPassWhenSchedulesArePresent() throws MyException {
        BookingCreateDTO dto = new BookingCreateDTO(1L, null, null, null, List.of(1L, 2L), List.of());
        assertDoesNotThrow(() -> validator.validate(dto));
    }

    @Test
    void shouldThrowWhenSchedulesAreEmpty() {
        BookingCreateDTO dto = new BookingCreateDTO(1L, null, null, null, List.of(), List.of());
        assertThrows(MyException.class, () -> validator.validate(dto));
    }

    @Test
    void shouldThrowWhenSchedulesAreNull() {
        BookingCreateDTO dto = new BookingCreateDTO(1L, null, null, null, null, List.of());
        assertThrows(MyException.class, () -> validator.validate(dto));
    }
}
