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

        // ✅ Verificación de imágenes
        boolean tieneFotoPerfil = user.getProfilePhoto() != null;

        boolean tieneFotosVerificacion = true;
        if (user.getRole() == Role.SITTER) {
            int cantidadFotos = imageRepository.countByUserId(user.getId());
            tieneFotosVerificacion = cantidadFotos > 0;
        }

        // ✅ Estado del perfil
        boolean completo = user.getName() != null &&
                user.getPhone() != null &&
                user.getLocation() != null &&
                tieneFotoPerfil &&
                tieneFotosVerificacion;

        user.setProfileComplete(completo);
        if (completo){
            user.setChecked(true);
        }
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

        // ✅ Verificación de imágenes
        boolean tieneFotoPerfil = user.getProfilePhoto() != null;

        boolean tieneFotosVerificacion = true;
        if (user.getRole() == Role.SITTER) {
            int cantidadFotos = imageRepository.countByUserId(user.getId());
            tieneFotosVerificacion = cantidadFotos > 0;
        }

        // ✅ Estado del perfil
        boolean completo = user.getName() != null &&
                user.getPhone() != null &&
                user.getLocation() != null &&
                tieneFotoPerfil &&
                tieneFotosVerificacion;

        user.setProfileComplete(completo);
        if (completo){
            user.setChecked(true);
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

    public void uploadVerifyIdentityPhoto(List<MultipartFile> imageFiles) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) auth.getPrincipal();

        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (user.getRole() != Role.SITTER) {
            throw new IllegalArgumentException("Solo las SITTER pueden cargar fotos de verificacion de identidad");
        }
        if (!user.getPhotosVerifyIdentity().isEmpty()) {
            throw new IllegalArgumentException("ya tienes fotos de verificación de identidad");
        }

        if (user.getPhotosVerifyIdentity().size() + imageFiles.size() > 3) {
            throw new IllegalArgumentException("no puedes subir mas de 3 fotos de verificacion de identidad");
        }
        for (MultipartFile file : imageFiles) {
            if (!file.isEmpty()) {
                imageValidator.validate(file); // Validar formato/tamaño
                byte[] optimizedImage = imageTreatment.process(file); // Comprimir

                Image image = new Image();
                image.setImageName(file.getOriginalFilename());
                image.setImageType(file.getContentType());
                image.setData(optimizedImage);

                // ✅ En vez de save manual, se guarda por cascada
                user.getPhotosVerifyIdentity().add(image);
            }
        }

        user.setProfileComplete(true);
        userRepository.save(user); // cascade guarda también las imágenes
    }

    public void updateVerifyIdentityPhoto(List<MultipartFile> imagesFiles) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) auth.getPrincipal();

        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (user.getRole() != Role.SITTER) {
            throw new IllegalArgumentException("Solo las SITTER pueden actualizar fotos de verificación de identidad");
        }

        // Validar que se envíen 3 imágenes
        if (imagesFiles == null || imagesFiles.size() != 3) {
            throw new IllegalArgumentException("Debes enviar exactamente 3 imágenes para la verificación de identidad");
        }

        // Si ya tenía fotos, vaciamos la lista para reemplazarlas
        user.getPhotosVerifyIdentity().clear();

        // Iterar sobre cada archivo y crear nuevas entidades Image
        for (MultipartFile file : imagesFiles) {
            imageValidator.validate(file);
            byte[] processedBytes = imageTreatment.process(file);

            Image image = new Image();
            image.setImageName(file.getOriginalFilename());
            image.setImageType(file.getContentType());
            image.setData(processedBytes);

            user.getPhotosVerifyIdentity().add(image);
        }

        // Guardar usuario con sus nuevas imágenes
        userRepository.save(user);
    }
}