package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.ComboOfferingCreateDTO;
import com.petcare.back.domain.dto.request.ScheduleConfigCreateDTO;
import com.petcare.back.domain.dto.request.ScheduleTurnCreateDTO;
import com.petcare.back.domain.entity.Offering;
import com.petcare.back.domain.enumerated.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class TestDataFactory {

    public static Offering mockOffering(Long id, OfferingEnum name, String description) {
        return new Offering(
                id,
                name,
                description,
                new BigDecimal("100"),
                List.of(PetTypeEnum.PERRO),
                ProfessionalRoleEnum.CUIDADOR,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public static ComboOfferingCreateDTO mockComboDTO(ComboEnum comboEnum, double discount, List<Long> offeringIds) {
        return new ComboOfferingCreateDTO(
                comboEnum,
                "Combo de prueba: " + comboEnum.getLabel(),
                discount,
                offeringIds
        );
    }

    public static List<Offering> mockOfferingsForCombo(ComboEnum comboEnum) {
        return comboEnum.getExpectedTypes().stream()
                .map(type -> mockOffering((long) type.ordinal(), type, type.name() + " estándar"))
                .collect(Collectors.toList());
    }

    public static Offering mockOfferingWithPetType(Long id, OfferingEnum name, String description, PetTypeEnum tipo) {
        return new Offering(
                id,
                name,
                description,
                new BigDecimal("100"),
                List.of(tipo),
                ProfessionalRoleEnum.CUIDADOR,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public static Offering mockOfferingWithPrice(Long id, OfferingEnum name, BigDecimal price) {
        return new Offering(
                id,
                name,
                name.name() + " premium",
                price,
                List.of(PetTypeEnum.PERRO),
                ProfessionalRoleEnum.CUIDADOR,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
    public static Offering mockSimpleOffering(Long id, OfferingEnum name) {
        return new Offering(
                id,
                name,
                name.name() + " básico",
                new BigDecimal("100"),
                List.of(PetTypeEnum.PERRO),
                ProfessionalRoleEnum.CUIDADOR,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public static ScheduleTurnCreateDTO mockTurn(
            WeekDayEnum day,
            LocalTime startTime,
            LocalTime endTime,
            LocalTime breakStart,
            LocalTime breakEnd
    ) {
        return new ScheduleTurnCreateDTO(day, startTime, endTime, breakStart, breakEnd);
    }

    public static List<ScheduleTurnCreateDTO> validTurns() {
        return List.of(
                mockTurn(WeekDayEnum.LUNES, LocalTime.of(9, 0), LocalTime.of(17, 0), LocalTime.of(12, 0), LocalTime.of(13, 0)),
                mockTurn(WeekDayEnum.MIERCOLES, LocalTime.of(9, 0), LocalTime.of(17, 0), LocalTime.of(12, 0), LocalTime.of(13, 0))
        );
    }

    public static List<ScheduleTurnCreateDTO> breakOutsideTurn() {
        return List.of(
                mockTurn(WeekDayEnum.LUNES, LocalTime.of(9, 0), LocalTime.of(17, 0), LocalTime.of(8, 0), LocalTime.of(9, 0))
        );
    }

    public static List<ScheduleTurnCreateDTO> tooShortTurn() {
        return List.of(
                mockTurn(WeekDayEnum.LUNES, LocalTime.of(9, 0), LocalTime.of(10, 0), null, null)
        );
    }

    public static List<ScheduleTurnCreateDTO> noServiceFitsTurn() {
        return List.of(
                mockTurn(WeekDayEnum.LUNES, LocalTime.of(9, 0), LocalTime.of(10, 0), LocalTime.of(9, 0), LocalTime.of(10, 0))
        );
    }

    public static ScheduleConfigCreateDTO mockScheduleDTO(
            LocalDate startDate,
            LocalDate endDate,
            int serviceDuration,
            int interval,
            List<ScheduleTurnCreateDTO> turns
    ) {
        return new ScheduleConfigCreateDTO(
                "Turno de prueba",
                startDate,
                endDate,
                serviceDuration,
                interval,
                turns
        );
    }
}
