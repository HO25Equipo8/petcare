package com.petcare.back.domain.dto.request;

public record BookingDataByEmailDTO(
        String userEmail,
        String ownerName,
        String professionalName,
        String petName,
        String sessionDate,
        String startTime,
        String endTime
) {
}
