package com.petcare.back.domain.dto.request;

import java.time.Instant;
public record ScheduleRescheduleDTO(
        Instant newTime
) {
}
