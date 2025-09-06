package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.PlanCreateDTO;
import com.petcare.back.domain.dto.response.PlanResponseDTO;
import com.petcare.back.domain.entity.Plan;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.FrequencyEnum;
import com.petcare.back.domain.enumerated.IntervalEnum;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.domain.mapper.request.PlanCreateMapper;
import com.petcare.back.domain.mapper.response.PlanResponseMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.PlanDiscountRuleRepository;
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
    @Mock private PlanDiscountRuleRepository planDiscountRuleRepository;

    @Mock private ValidatePlanFrecuenciaIntervaloCoherente frecuenciaValidator;
    @Mock(lenient = true) private ValidatePlanNombreDuplicado nombreValidator;

    @Mock private UserRepository userRepository;

    private PlanService planService;

    @BeforeEach
    void setup() {
        List<ValidationPlanCreate> validators = List.of(frecuenciaValidator, nombreValidator);
        planService = new PlanService(
                planRepository,
                planMapper,
                planResponseMapper,
                planDiscountRuleRepository,
                validators,
                userRepository
        );

        User mockUser = new User();
        mockUser.setRole(Role.OWNER);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(mockUser, null));
    }

    @Test
    void shouldThrowIfValidationFails() throws MyException {
        PlanCreateDTO dto = new PlanCreateDTO(FrequencyEnum.DIARIO, IntervalEnum.MENSUAL);

        doThrow(new MyException("Frecuencia inválida")).when(frecuenciaValidator).validate(dto);

        MyException ex = assertThrows(MyException.class, () -> planService.createPlan(dto));
        assertEquals("Frecuencia inválida", ex.getMessage());
    }

    @Test
    void shouldPassIfAllValidatorsApprove() throws MyException {
        PlanCreateDTO dto = new PlanCreateDTO(FrequencyEnum.TRES_POR_SEMANA, IntervalEnum.SEMANAL);

        doNothing().when(frecuenciaValidator).validate(dto);
        doNothing().when(nombreValidator).validate(dto);

        Plan mockPlan = new Plan();
        mockPlan.setId(1L);
        mockPlan.setName("Plan 3 veces por semana Semanal");
        mockPlan.setTimesPerWeek(3.0);
        mockPlan.setIntervalEnum(IntervalEnum.SEMANAL);
        mockPlan.setPromotion(15.0);

        when(planMapper.toEntity(dto)).thenReturn(mockPlan);
        when(planRepository.save(mockPlan)).thenReturn(mockPlan);
        when(planResponseMapper.toDto(mockPlan)).thenReturn(
                new PlanResponseDTO(1L, "Plan 3 veces por semana Semanal", 3, IntervalEnum.SEMANAL, 15.0)
        );

        assertDoesNotThrow(() -> planService.createPlan(dto));
    }
}

