package com.petcare.back.domain.dto.request;

import java.time.LocalDateTime;

public record TrackingNotificationDTO(
        Double lat,
        Double lng,
        String type,
        LocalDateTime time
) {
    public TrackingNotificationDTO(Double lat, Double lng, LocalDateTime time) {
        this(lat, lng, "LOCATION_UPDATE", time);
    }
}
