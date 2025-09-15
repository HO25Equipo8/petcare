package com.petcare.back.domain.dto.response;

import com.petcare.back.domain.enumerated.IncidentsTypes;

import java.time.Instant;

public record IncidentSessionResponseDTO(
        Long incidentId,
        IncidentsTypes incidentsType,
        String description,
        Instant incidentsDate,
        Long sessionId,
        Long bookingId,
        String petName,
        String ownerName
) {}
