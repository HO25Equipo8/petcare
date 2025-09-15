package com.petcare.back.domain.dto.request;

import java.time.LocalDateTime;

public record TrackingPointNotificationDTO(
        Long sessionId,
        Double lat,
        Double lng,
        LocalDateTime timestamp
) {}

