package com.petcare.back.domain.dto.response;

import com.petcare.back.domain.enumerated.BookingStatusEnum;
import java.time.Instant;
import java.util.List;

public record BookingListDTO(
        Long bookingId,
        String petName,
        String serviceTitle,
        Instant reservationDate,
        BookingStatusEnum status,
        Instant firstScheduleStart,
        Long comboId,
        List<ServiceItemResponseDTO> items
) {
}
