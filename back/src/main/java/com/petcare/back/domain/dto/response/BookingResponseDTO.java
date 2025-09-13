package com.petcare.back.domain.dto.response;

import com.petcare.back.domain.enumerated.BookingStatusEnum;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record BookingResponseDTO(
        Long id,
        String ownerName,
        String petName,
        String comboOfferingName,
        Instant reservationDate,
        BookingStatusEnum status,
        BigDecimal finalPrice,
        List<ServiceItemResponseDTO> items
) {
}
