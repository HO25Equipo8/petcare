package com.petcare.back.domain.dto.request;

import com.petcare.back.domain.enumerated.WeekDayEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ScheduleConfigCreateDTO(

        @NotBlank
        String configurationName,

        @NotNull
        LocalDate startDate,

        @NotNull
        LocalDate endDate,

        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        @Schema(type = "string", example = "09:00")
        LocalTime startTime,

        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        @Schema(type = "string", example = "17:00")
        LocalTime endTime,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        @Schema(type = "string", example = "12:00")
        LocalTime breakStart,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        @Schema(type = "string", example = "13:00")
        LocalTime breakEnd,

        @NotNull
        @Schema(example = "45")
        Integer serviceDurationMinutes,

        @NotNull
        @Schema(example = "10")
        Integer intervalBetweenServices,

        @NotNull
        @Schema(example = "[\"LUNES\", \"MIERCOLES\", \"VIERNES\"]")
        List<WeekDayEnum> days

) {}

