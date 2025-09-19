package com.petcare.back.domain.dto.response;

import com.petcare.back.domain.enumerated.BookingStatusEnum;
import java.math.BigDecimal;

public record ServiceItemResponseDTO(
        Long itemId,
        String offeringName,
        String professionalName,
        String professionalRole,
        BookingStatusEnum status,
        String scheduleDescription,
        BigDecimal price
) {
}
