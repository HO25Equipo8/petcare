package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.ComboOfferingCreateDTO;
import com.petcare.back.domain.entity.Offering;
import com.petcare.back.domain.enumerated.ComboEnum;
import com.petcare.back.domain.enumerated.OfferingEnum;
import com.petcare.back.domain.enumerated.PetTypeEnum;
import com.petcare.back.exception.MyException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidateComboPetTypeCoherenceTest {

    private final ValidateComboPetTypeCoherence validator = new ValidateComboPetTypeCoherence();

    @Test
    void shouldThrowIfOnlyTipoOTROAndIncludesAseo() {
        Offering o = TestDataFactory.mockOfferingWithPetType(1L, OfferingEnum.ASEO, "Aseo para tipo OTRO", PetTypeEnum.OTRO);
        List<Offering> offerings = List.of(o);
        ComboOfferingCreateDTO dto = TestDataFactory.mockComboDTO(ComboEnum.LIMPIO_Y_SANO, 10, List.of(1L));

        assertThrows(MyException.class, () -> validator.validate(dto, offerings));
    }

    @Test
    void shouldPassIfIncludesOtherPetTypes() {
        Offering o = TestDataFactory.mockOfferingWithPetType(1L, OfferingEnum.ASEO, "Aseo para perro", PetTypeEnum.PERRO);
        List<Offering> offerings = List.of(o);
        ComboOfferingCreateDTO dto = TestDataFactory.mockComboDTO(ComboEnum.LIMPIO_Y_SANO, 10, List.of(1L));

        assertDoesNotThrow(() -> validator.validate(dto, offerings));
    }
}


