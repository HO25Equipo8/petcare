package com.petcare.back.domain.dto.response;

import com.petcare.back.domain.enumerated.WeekDayEnum;

import java.time.LocalTime;

public record ScheduleTurnResponseDTO(
        WeekDayEnum day,
        LocalTime startTime,
        LocalTime endTime,
        LocalTime breakStart,
        LocalTime breakEnd
) {}
