package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.PlanCreateDTO;
import com.petcare.back.domain.dto.response.PlanResponseDTO;
import com.petcare.back.domain.entity.Plan;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.PlanType;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.domain.mapper.request.PlanCreateMapper;
import com.petcare.back.domain.mapper.response.PlanResponseMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.PlanRepository;
import com.petcare.back.repository.UserRepository;
import com.petcare.back.service.PlanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PlanCreateValidationServiceTest {

    @Mock private PlanRepository planRepository;
    @Mock private PlanCreateMapper planMapper;
    @Mock private PlanResponseMapper planResponseMapper;
    @Mock private UserRepository userRepository;
    @Mock private ValidatePlanTypeDuplicado tipoDuplicadoValidator;
    @Mock private ValidatePlanPrecioPositivo precioValidator;
    @Mock private ValidatePlanFuncionalidadActiva funcionalidadValidator;

    private PlanService planService;

    @BeforeEach
    void setup() {
        List<ValidationPlanCreate> validators = List.of(
                tipoDuplicadoValidator,
                precioValidator,
                funcionalidadValidator
        );

        planService = new PlanService(
                planRepository,
                planMapper,
                planResponseMapper,
                userRepository,
                validators
        );

        User mockUser = new User();
        mockUser.setRole(Role.ADMIN); // solo admin puede crear planes
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(mockUser, null));
    }

    @Test
    void shouldThrowIfValidationFails() throws MyException {
        PlanCreateDTO dto = new PlanCreateDTO(
                PlanType.PRO,
                100.0,
                "Acceso completo",
                true,
                true
        );

        doThrow(new MyException("Tipo duplicado")).when(tipoDuplicadoValidator).validate(dto);

        MyException ex = assertThrows(MyException.class, () -> planService.createPlan(dto));
        assertEquals("Tipo duplicado", ex.getMessage());
    }

    @Test
    void shouldPassIfAllValidatorsApprove() throws MyException {
        PlanCreateDTO dto = new PlanCreateDTO(
                PlanType.PREMIUM,
                50.0,
                "Actualizaciones en vivo",
                false,
                true
        );

        doNothing().when(tipoDuplicadoValidator).validate(dto);
        doNothing().when(precioValidator).validate(dto);
        doNothing().when(funcionalidadValidator).validate(dto);

        Plan mockPlan = new Plan();
        mockPlan.setId(1L);
        mockPlan.setType(PlanType.PREMIUM);
        mockPlan.setPrice(50.0);
        mockPlan.setDescription("Actualizaciones en vivo");
        mockPlan.setTrackingEnabled(false);
        mockPlan.setLiveUpdatesEnabled(true);

        when(planMapper.toEntity(dto)).thenReturn(mockPlan);
        when(planRepository.save(mockPlan)).thenReturn(mockPlan);
        when(planResponseMapper.toDto(mockPlan)).thenReturn(
                new PlanResponseDTO(1L, PlanType.PREMIUM, 50.0, "Actualizaciones en vivo", false, true)
        );

        assertDoesNotThrow(() -> planService.createPlan(dto));
    }
}

