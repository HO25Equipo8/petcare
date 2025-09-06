package com.petcare.back.domain.dto.response;

import com.petcare.back.domain.enumerated.IntervalEnum;

import java.time.LocalDate;

public record PlanResponseDTO(
        Long id,
        String name,
        Integer timesPerWeek,
        IntervalEnum intervalEnum
) {
}
