package com.petcare.back.domain.dto.response;

import com.petcare.back.domain.enumerated.ScheduleStatus;
import com.petcare.back.domain.enumerated.WeekDayEnum;

import java.time.Instant;

public record ScheduleResponseDTO(
        Long id,
        Instant time,
        ScheduleStatus status,
        WeekDayEnum day,
        String configName
) {
}
