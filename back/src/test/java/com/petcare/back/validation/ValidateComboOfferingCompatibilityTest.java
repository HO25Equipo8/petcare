package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.ComboOfferingCreateDTO;
import com.petcare.back.domain.entity.Offering;
import com.petcare.back.domain.enumerated.ComboEnum;
import com.petcare.back.domain.enumerated.OfferingEnum;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidateComboOfferingCompatibilityTest {

    private final ValidateComboOfferingCompatibility evaluator = new ValidateComboOfferingCompatibility();

    @Test
    void shouldReturnWarningForMultipleNocturnosNoFlexibles() {
        Offering o1 = TestDataFactory.mockOffering(1L, OfferingEnum.GUARDERIA, "Guardería nocturna");
        Offering o2 = TestDataFactory.mockOffering(2L, OfferingEnum.PASEO, "Paseo nocturno");

        List<Offering> offerings = List.of(o1, o2);
        ComboOfferingCreateDTO dto = TestDataFactory.mockComboDTO(ComboEnum.TRIPLE_CONFORT, 10, List.of(1L, 2L));

        List<String> warnings = evaluator.evaluar(dto, offerings);
        assertFalse(warnings.isEmpty());
    }

    @Test
    void shouldReturnEmptyIfCompatible() {
        Offering o1 = TestDataFactory.mockOffering(1L, OfferingEnum.GUARDERIA, "Guardería flexible");
        Offering o2 = TestDataFactory.mockOffering(2L, OfferingEnum.PASEO, "Paseo diurno");

        List<Offering> offerings = List.of(o1, o2);
        ComboOfferingCreateDTO dto = TestDataFactory.mockComboDTO(ComboEnum.TRIPLE_CONFORT, 10, List.of(1L, 2L));

        List<String> warnings = evaluator.evaluar(dto, offerings);
        assertTrue(warnings.isEmpty());
    }
}
