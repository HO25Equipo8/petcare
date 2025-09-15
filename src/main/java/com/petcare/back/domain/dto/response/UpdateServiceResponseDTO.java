package com.petcare.back.domain.dto.response;

import java.util.List;

public record UpdateServiceResponseDTO(
        Long id,
        String title,
        String message,
        String imageUrl
) {
}
