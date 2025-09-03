package com.petcare.back.domain.dto.response;

import com.petcare.back.domain.enumerated.WeekDayEnum;

import java.time.LocalDate;
import java.util.List;

public record ScheduleConfigResponseDTO(
        Long id,
        String configurationName,
        String sitterName,
        LocalDate startDate,
        LocalDate endDate,
        List<WeekDayEnum> days,
        List<ScheduleTurnResponseDTO> turns,
        Integer totalSchedulesGenerated
) {
}
