package com.petcare.back.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.petcare.back.domain.enumerated.WeekDayEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record ScheduleTurnCreateDTO(
        @NotNull
        WeekDayEnum day,
        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        @Schema(type = "string", example = "07:00")
        LocalTime startTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        @Schema(type = "string", example = "12:00")
        LocalTime endTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        @Schema(type = "string", example = "09:30")
        LocalTime breakStart,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        @Schema(type = "string", example = "10:00")
        LocalTime breakEnd
) {
}
