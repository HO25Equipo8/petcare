package com.petcare.back.validation;

import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class ValidateUserProfilePhoto implements ValidationFile{
    private static final List<String> ALLOWED_TYPES = List.of("image/png", "image/jpeg", "image/gif", "image/webp");
    private static final long MAX_SIZE = 5 * 1024 * 1024;

    @Override
    public void validate(MultipartFile file) throws MyException {
        if (file == null || file.isEmpty()) return;

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            throw new MyException("Formato de imagen no válido. Solo se permiten PNG, JPG, JPEG, GIF y WEBP.");
        }

        if (file.getSize() > MAX_SIZE) {
            throw new MyException("La imagen de perfil excede el tamaño máximo de 5MB.");
        }
    }
}

