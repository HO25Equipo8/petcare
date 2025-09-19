package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.ComboOfferingCreateDTO;
import com.petcare.back.domain.entity.Offering;
import com.petcare.back.domain.enumerated.ComboEnum;
import com.petcare.back.domain.enumerated.OfferingEnum;
import com.petcare.back.exception.MyException;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidateComboOfferingNotExistingTest {

    private final ValidateComboOfferingNotExisting validator = new ValidateComboOfferingNotExisting();

    @Test
    void shouldThrowIfOfferingIdNotFound() {
        ComboOfferingCreateDTO dto = TestDataFactory.mockComboDTO(ComboEnum.PASEO_ASEO, 10, List.of(1L, 2L));
        List<Offering> offerings = List.of(
                TestDataFactory.mockSimpleOffering(1L, OfferingEnum.PASEO) // Falta el 2L
        );

        assertThrows(MyException.class, () -> validator.validate(dto, offerings));
    }

    @Test
    void shouldPassIfAllIdsFound() {
        List<Offering> offerings = List.of(
                TestDataFactory.mockSimpleOffering(1L, OfferingEnum.PASEO),
                TestDataFactory.mockSimpleOffering(2L, OfferingEnum.ASEO)
        );
        ComboOfferingCreateDTO dto = TestDataFactory.mockComboDTO(ComboEnum.PASEO_ASEO, 10, List.of(1L, 2L));

        assertDoesNotThrow(() -> validator.validate(dto, offerings));
    }
}

