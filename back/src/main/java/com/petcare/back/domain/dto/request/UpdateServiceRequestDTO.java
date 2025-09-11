package com.petcare.back.domain.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record UpdateServiceRequestDTO(
        String title,
        String message,
        MultipartFile file
) {
}
