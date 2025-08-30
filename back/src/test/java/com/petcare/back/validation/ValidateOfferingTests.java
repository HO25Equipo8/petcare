package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.OfferingCreateDTO;
import com.petcare.back.domain.entity.Offering;
import com.petcare.back.domain.enumerated.OfferingEnum;
import com.petcare.back.domain.enumerated.PetTypeEnum;
import com.petcare.back.domain.enumerated.ProfessionalRoleEnum;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.OfferingRepository;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateOfferingTests {

    private final ValidateOfferingPrice validatePrice = new ValidateOfferingPrice();
    private final ValidateOfferingPetTypes validatePetTypes = new ValidateOfferingPetTypes();
    private final ValidateOfferingName validateName = new ValidateOfferingName();
    private final ValidateOfferingRoleCompatibility validateRoleCompatibility = new ValidateOfferingRoleCompatibility();

    @Mock
    private OfferingRepository offeringRepository;

    @Test
    void precioBaseCero_debeFallar() {
        var dto = new OfferingCreateDTO(OfferingEnum.PASEO, "desc", BigDecimal.ZERO, List.of(PetTypeEnum.PERRO), ProfessionalRoleEnum.PASEADOR);
        assertThrows(MyException.class, () -> validatePrice.validar(dto));
    }

    @Test
    void precioBaseNegativo_debeFallar() {
        var dto = new OfferingCreateDTO(OfferingEnum.PASEO, "desc", BigDecimal.valueOf(-100), List.of(PetTypeEnum.PERRO), ProfessionalRoleEnum.PASEADOR);
        assertThrows(MyException.class, () -> validatePrice.validar(dto));
    }

    @Test
    void sinTipoDeMascota_debeFallar() {
        var dto = new OfferingCreateDTO(OfferingEnum.PASEO, "desc", BigDecimal.valueOf(1000), List.of(), ProfessionalRoleEnum.PASEADOR);
        assertThrows(MyException.class, () -> validatePetTypes.validar(dto));
    }

    @Test
    void tipoDeMascotaValido_debePasar() {
        var dto = new OfferingCreateDTO(OfferingEnum.PASEO, "desc", BigDecimal.valueOf(1000), List.of(PetTypeEnum.PERRO), ProfessionalRoleEnum.PASEADOR);
        assertDoesNotThrow(() -> validatePetTypes.validar(dto));
    }

    @Test
    void nombreNulo_debeFallar() {
        var dto = new OfferingCreateDTO(null, "desc", BigDecimal.valueOf(1000), List.of(PetTypeEnum.PERRO), ProfessionalRoleEnum.PASEADOR);
        assertThrows(MyException.class, () -> validateName.validar(dto));
    }

    @Test
    void rolIncompatible_debeFallar() {
        var dto = new OfferingCreateDTO(OfferingEnum.ASEO, "desc", BigDecimal.valueOf(1000), List.of(PetTypeEnum.PERRO), ProfessionalRoleEnum.VETERINARIO);
        assertThrows(MyException.class, () -> validateRoleCompatibility.validar(dto));
    }

    @Test
    void rolCompatible_debePasar() {
        var dto = new OfferingCreateDTO(OfferingEnum.ASEO, "desc", BigDecimal.valueOf(1000), List.of(PetTypeEnum.PERRO), ProfessionalRoleEnum.PELUQUERO);
        assertDoesNotThrow(() -> validateRoleCompatibility.validar(dto));
    }

    @Test
    void servicioDuplicadoConTiposIguales_debeFallar() {
        var dto = new OfferingCreateDTO(
                OfferingEnum.PASEO,
                "desc",
                BigDecimal.valueOf(1000),
                List.of(PetTypeEnum.PERRO, PetTypeEnum.GATO),
                ProfessionalRoleEnum.PASEADOR
        );

        var existingOffering = new Offering();
        existingOffering.setName(OfferingEnum.PASEO);
        existingOffering.setApplicablePetTypes(List.of(PetTypeEnum.GATO, PetTypeEnum.PERRO)); // mismo conjunto

        when(offeringRepository.findByName(OfferingEnum.PASEO))
                .thenReturn(List.of(existingOffering));

        var validator = new ValidateOfferingUniqueness(offeringRepository);
        assertThrows(MyException.class, () -> validator.validar(dto));
    }
    @Test
    void descripcionVacia_debeFallar() {
        var dto = new OfferingCreateDTO(OfferingEnum.PASEO, "   ", BigDecimal.valueOf(1000), List.of(PetTypeEnum.PERRO), ProfessionalRoleEnum.PASEADOR);
        assertThrows(ConstraintViolationException.class, () -> {
            Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
            validator.validate(dto).stream().findFirst().ifPresent(violation -> { throw new ConstraintViolationException(Set.of(violation)); });
        });
    }

    @Test
    void rolNulo_debeFallar() {
        var dto = new OfferingCreateDTO(OfferingEnum.PASEO, "desc", BigDecimal.valueOf(1000), List.of(PetTypeEnum.PERRO), null);
        assertThrows(ConstraintViolationException.class, () -> {
            Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
            validator.validate(dto).stream().findFirst().ifPresent(violation -> { throw new ConstraintViolationException(Set.of(violation)); });
        });
    }

    @Test
    void precioBaseAlto_debePasar() {
        var dto = new OfferingCreateDTO(OfferingEnum.PASEO, "desc", BigDecimal.valueOf(99999), List.of(PetTypeEnum.PERRO), ProfessionalRoleEnum.PASEADOR);
        assertDoesNotThrow(() -> validatePrice.validar(dto));
    }

    @Test
    void servicioNuevoConTiposDistintos_debePasar() {
        var dto = new OfferingCreateDTO(
                OfferingEnum.PASEO,
                "desc",
                BigDecimal.valueOf(1000),
                List.of(PetTypeEnum.PERRO),
                ProfessionalRoleEnum.PASEADOR
        );

        var existingOffering = new Offering();
        existingOffering.setName(OfferingEnum.PASEO);
        existingOffering.setApplicablePetTypes(List.of(PetTypeEnum.GATO)); // distinto

        when(offeringRepository.findByName(OfferingEnum.PASEO))
                .thenReturn(List.of(existingOffering));

        var validator = new ValidateOfferingUniqueness(offeringRepository);
        assertDoesNotThrow(() -> validator.validar(dto));
    }
}
