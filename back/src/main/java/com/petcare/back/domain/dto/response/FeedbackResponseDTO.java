package com.petcare.back.domain.dto.response;

import com.petcare.back.domain.enumerated.FeedbackContext;
import java.time.LocalDateTime;

public record FeedbackResponseDTO(
        Long id,
        String authorName,
        Integer rating,
        String comment,
        FeedbackContext context,
        String contextLabel,
        String contextDescription,
        LocalDateTime createdAt
) {}