package com.petcare.back.domain.dto.request;

import com.petcare.back.domain.enumerated.FeedbackContext;

public record FeedbackDTO(
        Long targetUserId,
        Long bookingId,
        Integer rating,
        String comment,
        FeedbackContext context
) {
}
