package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.ScheduleConfigCreateDTO;
import com.petcare.back.domain.entity.ScheduleConfig;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.ScheduleConfigRepository;
import com.petcare.back.repository.ScheduleRepository;
import com.petcare.back.service.ScheduleConfigService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@SpringBootTest
public class ValidateScheduleConfigServiceTest {

    @Autowired
    private ScheduleConfigService service;

    @MockBean
    private ScheduleRepository scheduleRepository;

    @MockBean
    private ScheduleConfigRepository scheduleConfigRepository;

    @BeforeEach
    void setupAuth() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setRole(Role.SITTER);
        mockUser.setName("Sitter Test");
        mockUser.setChecked(true);
        mockUser.setProfileComplete(true);

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(mockUser);
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldPassWithValidDTO() {
        ScheduleConfigCreateDTO dto = TestDataFactory.mockScheduleDTO(
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                45,
                10,
                TestDataFactory.validTurns()
        );

        when(scheduleConfigRepository.save(any(ScheduleConfig.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        assertDoesNotThrow(() -> service.createScheduleConfig(dto));
    }

    @Test
    void shouldFailIfStartDateAfterEndDate() {
        ScheduleConfigCreateDTO dto = TestDataFactory.mockScheduleDTO(
                LocalDate.now().plusDays(7),
                LocalDate.now(),
                45,
                10,
                TestDataFactory.validTurns()
        );

        assertThrows(MyException.class, () -> service.createScheduleConfig(dto));
    }

    @Test
    void shouldFailIfBreakOutsideShift() {
        ScheduleConfigCreateDTO dto = TestDataFactory.mockScheduleDTO(
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                45,
                10,
                TestDataFactory.breakOutsideTurn()
        );

        assertThrows(MyException.class, () -> service.createScheduleConfig(dto));
    }

    @Test
    void shouldFailIfDurationTooLong() {
        ScheduleConfigCreateDTO dto = TestDataFactory.mockScheduleDTO(
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                120,
                10,
                TestDataFactory.tooShortTurn()
        );

        assertThrows(MyException.class, () -> service.createScheduleConfig(dto));
    }

    @Test
    void shouldFailIfNoTurnsProvided() {
        ScheduleConfigCreateDTO dto = TestDataFactory.mockScheduleDTO(
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                45,
                10,
                List.of()
        );

        assertThrows(MyException.class, () -> service.createScheduleConfig(dto));
    }

    @Test
    void shouldFailIfNoServiceFits() {
        ScheduleConfigCreateDTO dto = TestDataFactory.mockScheduleDTO(
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                45,
                10,
                TestDataFactory.noServiceFitsTurn()
        );

        assertThrows(MyException.class, () -> service.createScheduleConfig(dto));
    }
}
