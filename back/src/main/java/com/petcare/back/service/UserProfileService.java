package com.petcare.back.service;

import com.petcare.back.domain.entity.Image;
import com.petcare.back.domain.entity.User;
import com.petcare.back.infra.error.ImageValidator;
import com.petcare.back.repository.ImageRepository;
import com.petcare.back.repository.UserRepository;
import jakarta.transaction.Transactional;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Transactional
@Service
public class UserProfileService {

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final ImageValidator imageValidator;

    public UserProfileService(UserRepository userRepository,
                              ImageRepository imageRepository,
                              ImageValidator imageValidator) {
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.imageValidator = imageValidator;
    }

    public void uploadProfilePhoto(MultipartFile file) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) auth.getPrincipal();

        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Validar formato/tamaño
        imageValidator.validate(file);

        // 3. Procesar la imagen (comprimir/redimensionar)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Thumbnails.of(file.getInputStream())
                .size(512, 512)       // perfil: cuadrado más pequeño
                .outputQuality(0.7)   // calidad reducida
                .toOutputStream(baos);

        // 4. Crear y guardar entidad Image
        Image img = new Image();
        img.setImageName(file.getOriginalFilename());
        img.setImageType(file.getContentType());
        img.setData(baos.toByteArray());

        imageRepository.save(img);

        // 5. Asignar al usuario y guardar
        user.setProfilePhoto(img);
        userRepository.save(user);
    }
}

