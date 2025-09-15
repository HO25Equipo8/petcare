package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.ComboOfferingCreateDTO;
import com.petcare.back.domain.entity.Offering;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@Component
public class ValidateComboPriceViability implements ValidationCombo {

    @Override
    public void validate(ComboOfferingCreateDTO dto, List<Offering> offerings) throws MyException {
        BigDecimal total = offerings.stream()
                .map(Offering::getBasePrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal discountPercentage = BigDecimal.valueOf(dto.discount());
        BigDecimal discount = total.multiply(discountPercentage)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal finalPrice = total.subtract(discount);

        BigDecimal minimumAllowed = total.multiply(BigDecimal.valueOf(0.6));

        if (finalPrice.compareTo(minimumAllowed) < 0) {
            throw new MyException("El descuento aplicado reduce demasiado el precio final. Verificar si es comercialmente viable.");
        }
    }
}

