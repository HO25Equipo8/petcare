package com.petcare.back.domain.dto.response;

public record ScheduleWithSitterDTO(
        Long scheduleId,
         String time,
         String status,
         String day,
         String configName,

        // Datos del sitter
         Long sitterId,
         String sitterName,
         String sitterEmail
) {
}
