package com.petcare.back.domain.dto.request;

import com.petcare.back.domain.enumerated.FrequencyEnum;
import com.petcare.back.domain.enumerated.IntervalEnum;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PlanCreateDTO(
        @NotNull FrequencyEnum frequencyEnum,
        @NotNull IntervalEnum intervalEnum
) {
}
