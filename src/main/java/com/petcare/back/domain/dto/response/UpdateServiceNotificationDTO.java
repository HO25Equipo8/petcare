package com.petcare.back.domain.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record UpdateServiceNotificationDTO(
        Long sessionId,
        String title,
        String message,
        String imageUrl,
        LocalDateTime timestamp
) {
}
