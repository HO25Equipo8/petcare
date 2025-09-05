package com.petcare.back.validation;

import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
@Component
public class ValidateUserProfileIdentityPhotos implements ValidationFileList {
    private static final long MAX_SIZE = 5 * 1024 * 1024;

    @Override
    public void validate(MultipartFile[] files, User authenticatedUse) throws MyException {

        if (authenticatedUse.getRole() != Role.SITTER || files == null || files.length == 0) return;

        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) continue;

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new MyException("El archivo " + file.getOriginalFilename() + " no es una imagen válida.");
            }

            if (file.getSize() > MAX_SIZE) {
                throw new MyException("La imagen " + file.getOriginalFilename() + " excede el tamaño máximo de 5MB.");
            }
        }
    }
}
