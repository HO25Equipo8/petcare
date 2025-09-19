package com.petcare.back.domain.dto.response;

import java.time.Instant;

public record ScheduleDropdownDTO(
        Long id,
        Instant establishedTime
) {
}
