package com.petcare.back.service;

import com.petcare.back.domain.entity.Image;
import com.petcare.back.domain.entity.Pet;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.infra.error.ImageTreatment;
import com.petcare.back.infra.error.ImageValidator;
import com.petcare.back.repository.ImageRepository;
import com.petcare.back.repository.PetRepository;
import com.petcare.back.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Transactional
@Service
public class UserProfileService {


    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final ImageValidator imageValidator;
    private final ImageTreatment imageTreatment;
    private final PetRepository petRepository;


    public UserProfileService(UserRepository userRepository,
                              ImageRepository imageRepository,
                              ImageValidator imageValidator,
                              ImageTreatment imageTreatment,
                              PetRepository petRepository) {
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.imageValidator = imageValidator;
        this.imageTreatment = imageTreatment;
        this.petRepository = petRepository;
    }

    public void uploadProfilePhoto(MultipartFile file) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) auth.getPrincipal();

        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (user.getProfilePhoto() != null) {
            throw new IllegalStateException("El usuario ya tiene una foto de perfil. Use el servicio de actualización.");
        }


        // Validar formato/tamaño
        imageValidator.validate(file);

        // 🔧 Procesar (redimensionar/comprimir) y obtener bytes optimizados
        byte[] processedBytes = imageTreatment.process(file);

        // Crear y guardar entidad Image
        Image img = new Image();
        img.setImageName(file.getOriginalFilename());
        img.setImageType(file.getContentType());
        img.setData(processedBytes);

        imageRepository.save(img);

        // Asignar al usuario y guardar
        user.setProfilePhoto(img);
        userRepository.save(user);
    }
    public void updateProfilePhoto(MultipartFile file) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) auth.getPrincipal();

        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Image image = user.getProfilePhoto();
        if (image == null) {
            image = new Image(); // si no tenía antes
        }

        imageValidator.validate(file);
        byte[] processedBytes = imageTreatment.process(file);

        image.setImageName(file.getOriginalFilename());
        image.setImageType(file.getContentType());
        image.setData(processedBytes);

        imageRepository.save(image);

        // Si antes no tenía, asignamos al user
        if (user.getProfilePhoto() == null) {
            user.setProfilePhoto(image);
            userRepository.save(user);
        }
    }



    public void uploadPrifilePhotoPet(Long petId, MultipartFile file) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) auth.getPrincipal();

        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (user.getRole() != Role.OWNER) {
            throw new IllegalArgumentException("Solo los owner pueden cargar foto de perfil a sus mascotas");
        }

        // Buscar la mascota
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        // Verificar que la mascota le pertenece al owner autenticado
        if (!pet.getOwner().getId().equals(user.getId())) {
            throw new IllegalArgumentException("No puedes modificar mascotas de otro usuario");
        }

        if(pet.getImagePet() != null) {
            throw new IllegalArgumentException("la mascota ya tiene una foto de perfil ");
        }

        // Validar formato/tamaño
        imageValidator.validate(file);

        // Procesar (redimensionar/comprimir)
        byte[] processedBytes = imageTreatment.process(file);

        // Crear imagen
        Image img = new Image();
        img.setImageName(file.getOriginalFilename());
        img.setImageType(file.getContentType());
        img.setData(processedBytes);
        imageRepository.save(img);

        // Asignar imagen a la mascota
        pet.setImagePet(img);
        petRepository.save(pet);
    }

    public void updateProfilePhotoPet(Long petId, MultipartFile file) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) auth.getPrincipal();

        // Buscar el dueño
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (user.getRole() != Role.OWNER) {
            throw new IllegalArgumentException("Solo los owner pueden actualizar foto de perfil a sus mascotas");
        }

        // Buscar la mascota
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        // Validar que esa mascota sea del dueño autenticado
        if (!pet.getOwner().getId().equals(user.getId())) {
            throw new IllegalArgumentException("No puedes actualizar una mascota que no es tuya");
        }

        // Tomar o crear la imagen
        Image image = pet.getImagePet();
        if (image == null) {
            image = new Image(); // si no tenía antes
        }

        // Validar y procesar la imagen
        imageValidator.validate(file);
        byte[] processedBytes = imageTreatment.process(file);

        image.setImageName(file.getOriginalFilename());
        image.setImageType(file.getContentType());
        image.setData(processedBytes);

        imageRepository.save(image);

        // Asignar si era la primera vez
        if (pet.getImagePet() == null) {
            pet.setImagePet(image);
            petRepository.save(pet);
        }
    }
    public void uploadPetGallery(Long petId, List<MultipartFile> imageFiles) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) auth.getPrincipal();

        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (user.getRole() != Role.OWNER) {
            throw new IllegalArgumentException("Solo los owner pueden cargar fotos  a sus mascotas");
        }

        // Buscar la mascota
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));


        if (!pet.getOwner().getId().equals(user.getId())) {
            throw new IllegalArgumentException("No puedes modificar mascotas de otro usuario");
        }

        for (MultipartFile file : imageFiles) {
            if (!file.isEmpty()) {
                imageValidator.validate(file); // Validar formato/tamaño
                byte[] optimizedImage = imageTreatment.process(file); // Comprimir

                Image image = new Image();
                image.setImageName(file.getOriginalFilename());
                image.setImageType(file.getContentType());
                image.setData(optimizedImage);
                image.setPet(pet);
                imageRepository.save(image);

            }
        }
       petRepository.save(pet);

    }
}