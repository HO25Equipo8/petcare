package com.petcare.back.domain.dto.response;

import com.petcare.back.domain.enumerated.BookingStatusEnum;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record BookingResponseDTO(
        Long id,
        String ownerName,                // nombre del dueño
        String petName,                  // nombre de la mascota
        String offeringName,             // nombre del servicio
        String comboOfferingName,        // nombre del combo, si aplica
        String planName,                 // nombre del plan, si aplica
        List<String> scheduleDescription,      // descripción o rango horario
        Instant reservationDate,
        BookingStatusEnum status,
        BigDecimal finalPrice,
        List<String> professionalNames
) {
}
