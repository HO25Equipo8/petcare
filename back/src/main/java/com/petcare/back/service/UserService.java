package com.petcare.back.service;

import com.petcare.back.domain.dto.request.UserUpdateDTO;
import com.petcare.back.domain.entity.Image;
import com.petcare.back.domain.entity.Location;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.ProfessionalRoleEnum;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.ImageRepository;
import com.petcare.back.repository.UserRepository;
import com.petcare.back.validation.ValidationFile;
import com.petcare.back.validation.ValidationFileList;
import com.petcare.back.validation.ValidationUserProfile;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final LocationService locationService;
    private final IncidentServiceImpl imageService;
    private final UserRepository userRepository;

    private final List<ValidationUserProfile> validations;
    private final ValidationFile validateProfilePhoto;
    private final ValidationFileList validateIdentityPhotos;
    private final ImageRepository imageRepository;

    @Transactional
    public User updateProfile(UserUpdateDTO dto,
                              MultipartFile profilePhoto,
                              MultipartFile[] identityPhotos) throws MyException, IOException {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // ✅ Datos básicos
        user.setName(dto.name());
        user.setPhone(dto.phone());

        if (dto.location() != null) {
            Location savedLocation = locationService.save(dto.location());
            user.setLocation(savedLocation);
        }

        // ✅ Validaciones modulares
        validateProfilePhoto.validate(profilePhoto);
        validateIdentityPhotos.validate(identityPhotos, user);
        for (ValidationUserProfile validation : validations) {
            validation.validate(dto, user);
        }

        // ✅ Procesamiento de imágenes
        if (profilePhoto != null && !profilePhoto.isEmpty()) {
            if (user.getProfilePhoto() != null) {
                imageRepository.delete(user.getProfilePhoto());
            }
            Image savedProfile = imageService.processImage(profilePhoto);
            user.setProfilePhoto(savedProfile);
        }

        if (identityPhotos != null && user.getRole() == Role.SITTER) {
            List<Image> savedPhotos = new ArrayList<>();
            for (MultipartFile file : identityPhotos) {
                if (file != null && !file.isEmpty()) {
                    try {
                        Image image = imageService.processImage(file);
                        savedPhotos.add(image);
                    } catch (IOException e) {
                        throw new MyException("Error al procesar imagen de verificación: " + file.getOriginalFilename());
                    }
                }
            }
            user.setPhotosVerifyIdentity(savedPhotos);
        }

        // ✅ Roles profesionales (ya validados)
        if (user.getRole() == Role.SITTER) {
            user.setProfessionalRoles(dto.professionalRoles());
        } else {
            user.setProfessionalRoles(new ArrayList<>());
        }

        // ✅ Estado del perfil y verificación (delegado si querés)
        boolean completo = user.getName() != null &&
                user.getPhone() != null &&
                user.getLocation() != null &&
                user.getProfilePhoto() != null &&
                (user.getRole() != Role.SITTER ||
                        (user.getPhotosVerifyIdentity() != null && !user.getPhotosVerifyIdentity().isEmpty()));

        user.setProfileComplete(completo);
        user.setVerified(user.getRole() == Role.OWNER);

        return userRepository.save(user);
    }

    public User getPublicProfile(Long id) throws MyException {
        return userRepository.findById(id)
                .orElseThrow(() -> new MyException("Usuario no encontrado"));
    }
    public List<User> findByRole(Role role) {
        return userRepository.findByRole(role);
    }
    public List<User> findTopSitters(Pageable pageable) {
        return userRepository.findTopSittersByReputationNative(pageable);
    }
}
