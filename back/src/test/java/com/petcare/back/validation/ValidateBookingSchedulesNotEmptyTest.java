package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.domain.dto.request.BookingServiceItemCreateDTO;
import com.petcare.back.exception.MyException;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidateServiceItemSchedulesNotEmptyTest {

    ValidateBookingSchedulesNotEmpty validator = new ValidateBookingSchedulesNotEmpty();

    @Test
    void shouldPassWhenAllItemsHaveSchedules() throws MyException {
        BookingServiceItemCreateDTO item1 = new BookingServiceItemCreateDTO(1L, 100L, 10L);
        BookingServiceItemCreateDTO item2 = new BookingServiceItemCreateDTO(2L, 101L, 11L);
        BookingCreateDTO dto = new BookingCreateDTO(1L, null, List.of(item1, item2));

        assertDoesNotThrow(() -> validator.validate(dto));
    }

    @Test
    void shouldThrowWhenAnyItemHasNullSchedule() {
        BookingServiceItemCreateDTO item1 = new BookingServiceItemCreateDTO(1L, null, 10L); // ❌ sin horario
        BookingServiceItemCreateDTO item2 = new BookingServiceItemCreateDTO(2L, 101L, 11L);
        BookingCreateDTO dto = new BookingCreateDTO(1L, null, List.of(item1, item2));

        assertThrows(MyException.class, () -> validator.validate(dto));
    }

    @Test
    void shouldThrowWhenItemsListIsEmpty() {
        BookingCreateDTO dto = new BookingCreateDTO(1L, null, List.of());
        assertThrows(MyException.class, () -> validator.validate(dto));
    }

    @Test
    void shouldThrowWhenItemsListIsNull() {
        BookingCreateDTO dto = new BookingCreateDTO(1L, null, null);
        assertThrows(MyException.class, () -> validator.validate(dto));
    }
}

