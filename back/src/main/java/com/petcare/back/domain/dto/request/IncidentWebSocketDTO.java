package com.petcare.back.domain.dto.request;

import java.time.Instant;

public record IncidentWebSocketDTO(
        String type,
        String description,
        Instant timestamp,
        Long ownerId,
        Long sitterId,
        Long petId
) {
}
