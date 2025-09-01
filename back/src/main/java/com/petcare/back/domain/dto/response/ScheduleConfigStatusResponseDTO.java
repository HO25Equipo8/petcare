package com.petcare.back.domain.dto.response;

import java.time.LocalDate;

public record ScheduleConfigStatusResponseDTO(
            boolean hasActiveConfig,
            LocalDate endDate
    ) {}

