package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.domain.dto.request.BookingServiceItemCreateDTO;
import com.petcare.back.domain.entity.Schedule;
import com.petcare.back.domain.entity.ScheduleConfig;
import com.petcare.back.domain.entity.User;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.ScheduleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class ValidateBookingServiceItemScheduleOwnershipTest {

    @InjectMocks
    private ValidateBookingServiceItemScheduleOwnership validator;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Test
    void shouldThrowExceptionWhenScheduleDoesNotBelongToProfessional() {
        // Setup: profesional con ID 1, pero el horario pertenece al profesional 2
        User sitter = new User();
        sitter.setId(2L);

        ScheduleConfig config = new ScheduleConfig();
        config.setSitter(sitter);

        Schedule schedule = new Schedule();
        schedule.setScheduleId(100L);
        schedule.setScheduleConfig(config);

        BookingServiceItemCreateDTO itemDTO = new BookingServiceItemCreateDTO(1L, 100L, 1L); // offeringId, scheduleId, professionalId
        BookingCreateDTO dto = new BookingCreateDTO(1L, 0L, List.of(itemDTO)); // petId, comboId, items

        Mockito.when(scheduleRepository.findAllById(List.of(100L)))
                .thenReturn(List.of(schedule));

        MyException ex = assertThrows(MyException.class, () -> validator.validate(dto));
        assertTrue(ex.getMessage().contains("100"));
    }

    @Test
    void shouldPassWhenScheduleBelongsToSelectedProfessional() {
        // Setup: profesional con ID 1, y el horario también pertenece a él
        User sitter = new User();
        sitter.setId(1L);

        ScheduleConfig config = new ScheduleConfig();
        config.setSitter(sitter);

        Schedule schedule = new Schedule();
        schedule.setScheduleId(101L);
        schedule.setScheduleConfig(config);

        BookingServiceItemCreateDTO itemDTO = new BookingServiceItemCreateDTO(1L, 101L, 1L);
        BookingCreateDTO dto = new BookingCreateDTO(1L, 0L, List.of(itemDTO));

        Mockito.when(scheduleRepository.findAllById(List.of(101L)))
                .thenReturn(List.of(schedule));

        assertDoesNotThrow(() -> validator.validate(dto));
    }
}
