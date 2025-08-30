package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.ComboOfferingCreateDTO;
import com.petcare.back.domain.entity.Offering;
import com.petcare.back.domain.enumerated.ComboEnum;
import com.petcare.back.domain.enumerated.OfferingEnum;
import com.petcare.back.domain.enumerated.PetTypeEnum;
import com.petcare.back.domain.enumerated.ProfessionalRoleEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
}
