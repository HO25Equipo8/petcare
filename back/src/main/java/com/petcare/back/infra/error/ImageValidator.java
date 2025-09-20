package com.petcare.back.infra.error;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class ImageValidator {

    private static final List<String> ALLOWED_TYPES = List.of(
            "image/png",
            "image/jpeg",
            "image/gif",
            "image/webp"
    );

    private static final long MAX_SIZE = 5 * 1024 * 1024; // 5MB

    public void validate(MultipartFile file) {
        String contentType = file.getContentType();

        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            throw new IllegalArgumentException(
                    "Formato no válido. Solo se permiten PNG, JPG, JPEG, GIF y WEBP."
            );
        }

        if (file.getSize() > MAX_SIZE) {
            throw new IllegalArgumentException("La imagen excede el tamaño máximo de 5MB.");
        }
    }
}

