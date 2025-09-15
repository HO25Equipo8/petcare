package com.petcare.back.domain.dto.request;

import jakarta.validation.constraints.NotNull;

public record StartSessionDTO(
        @NotNull
        Long bookingId
) {
}
