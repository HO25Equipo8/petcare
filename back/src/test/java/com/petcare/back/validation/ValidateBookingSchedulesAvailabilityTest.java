package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.domain.entity.Schedule;
import com.petcare.back.domain.enumerated.ScheduleStatus;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.ScheduleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ValidateBookingSchedulesAvailabilityTest {

    @Mock
    ScheduleRepository scheduleRepository;
    @InjectMocks
    ValidateBookingSchedulesAvailability validator;

    @Test
    void shouldPassWhenAllSchedulesAreAvailable() throws MyException {
        Schedule s1 = new Schedule(); s1.setScheduleId(1L); s1.setStatus(ScheduleStatus.DISPONIBLE);
        Schedule s2 = new Schedule(); s2.setScheduleId(2L); s2.setStatus(ScheduleStatus.DISPONIBLE);

        BookingCreateDTO dto = new BookingCreateDTO(1L, null, null, List.of(1L, 2L), List.of());
        when(scheduleRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(s1, s2));

        assertDoesNotThrow(() -> validator.validate(dto));
    }

    @Test
    void shouldThrowWhenSomeSchedulesAreUnavailable() {
        Schedule s1 = new Schedule(); s1.setScheduleId(1L); s1.setStatus(ScheduleStatus.RESERVADO);
        BookingCreateDTO dto = new BookingCreateDTO(1L, null, null, List.of(1L), List.of());

        when(scheduleRepository.findAllById(List.of(1L))).thenReturn(List.of(s1));

        MyException ex = assertThrows(MyException.class, () -> validator.validate(dto));
        assertTrue(ex.getMessage().contains("1"));
    }
}
