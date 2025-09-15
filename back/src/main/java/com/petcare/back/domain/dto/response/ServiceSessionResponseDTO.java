package com.petcare.back.domain.dto.response;

import com.petcare.back.domain.enumerated.ServiceSessionStatus;

import java.time.LocalDateTime;

public record ServiceSessionResponseDTO(
        Long sessionId,
        Long bookingId,
        ServiceSessionStatus status,
        LocalDateTime startTime
) { }
