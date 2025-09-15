package com.petcare.back.domain.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ScheduleConfigCreateDTO(
        @NotBlank
        String configurationName,
        @NotNull
        LocalDate startDate,
        @NotNull
        LocalDate endDate,
        @NotNull
        @Schema(example = "45")
        Integer serviceDurationMinutes,
        @NotNull
        @Schema(example = "10")
        Integer intervalBetweenServices,
        @NotNull
        @Size(min = 1, message = "Debes configurar al menos un turno.")
        List<ScheduleTurnCreateDTO> turns
) {
}

