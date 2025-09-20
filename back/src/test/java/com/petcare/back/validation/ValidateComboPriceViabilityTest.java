package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.ComboOfferingCreateDTO;
import com.petcare.back.domain.entity.Offering;
import com.petcare.back.domain.enumerated.ComboEnum;
import com.petcare.back.domain.enumerated.OfferingEnum;
import com.petcare.back.exception.MyException;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidateComboPriceViabilityTest {

    private final ValidateComboPriceViability validator = new ValidateComboPriceViability();

    @Test
    void shouldPassIfPriceIsAboveMinimumThreshold() {
        List<Offering> offerings = List.of(
                TestDataFactory.mockOfferingWithPrice(1L, OfferingEnum.ASEO, new BigDecimal("1000")),
                TestDataFactory.mockOfferingWithPrice(2L, OfferingEnum.PASEO, new BigDecimal("1000"))
        );
        ComboOfferingCreateDTO dto = TestDataFactory.mockComboDTO(ComboEnum.PASEO_ASEO, 30, List.of(1L, 2L)); // 30% → OK

        assertDoesNotThrow(() -> validator.validate(dto, offerings));
    }

    @Test
    void shouldThrowIfDiscountTooHigh() {
        List<Offering> offerings = List.of(
                TestDataFactory.mockOfferingWithPrice(1L, OfferingEnum.ASEO, new BigDecimal("1000")),
                TestDataFactory.mockOfferingWithPrice(2L, OfferingEnum.PASEO, new BigDecimal("1000"))
        );
        ComboOfferingCreateDTO dto = TestDataFactory.mockComboDTO(ComboEnum.PASEO_ASEO, 90, List.of(1L, 2L));

        assertThrows(MyException.class, () -> validator.validate(dto, offerings));
    }
}

