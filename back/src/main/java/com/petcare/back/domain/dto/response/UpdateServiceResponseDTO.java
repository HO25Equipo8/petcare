package com.petcare.back.domain.dto.response;

public record UpdateServiceResponseDTO(
        Long id,
        String title,
        String message,
        String imageUrl
) {
}
