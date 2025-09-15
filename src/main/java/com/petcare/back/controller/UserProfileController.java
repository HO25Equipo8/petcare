package com.petcare.back.controller;

import com.petcare.back.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@CrossOrigin(origins = "http://localhost:5173/", allowedHeaders = "*", allowCredentials = "true")
@SecurityRequirement(name = "bearer-key")
@RestController
@RequestMapping("/me")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }
    @Operation(summary = "Subir foto de perfil")
    @PostMapping(value = "/profile-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadProfilePhoto(@RequestParam("file") MultipartFile file) throws IOException {
        System.out.println(">>>> Entró al controller de profile-photo");
        try {
            userProfileService.uploadProfilePhoto(file);
            return ResponseEntity.ok("Foto de perfil subida con éxito");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error al procesar la imagen");
        }
    }
    @PutMapping(value = "/profile-photo-pet",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateProfilePhoto(@RequestParam("file") MultipartFile file) throws IOException {
        userProfileService.updateProfilePhoto(file);
        return ResponseEntity.ok("Foto de perfil actualizado del usuario");
    }


    @PutMapping(value = "/profile-photo-pet/{petId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateProfilePhotoPet(@PathVariable("petId") Long petId, @RequestParam("file") MultipartFile file) throws IOException {
   userProfileService.updateProfilePhotoPet(petId, file);
   return ResponseEntity.ok("Foto de perfil actualizado del pet");
    }


    @Operation(summary = "Subir foto de perfil del pet ")
    @PostMapping(value = "/profile-photo-pet/{petId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<String> uploadProfilePhotoPet(@RequestParam("file") MultipartFile file,@PathVariable Long petId) throws IOException {
        try {
            userProfileService.uploadPrifilePhotoPet(petId, file);
            return ResponseEntity.ok("Foto de perfil subida del  pet");
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body("Error al procesar la imagen" + e.getMessage());
        }catch (IOException e){
            return ResponseEntity.internalServerError().body("Error al procesar la imagen" + e.getMessage());
        }
    }

    @PostMapping(path = "/{petId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Subir galeria  de imagenes de un pet",
            description = "Cargar galeria de imagenes para el pet")
    public ResponseEntity<String> uploadImages(
            @PathVariable Long petId,
            @Parameter(description = "Imágenes a subir", required = true, content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary")))
            @RequestPart("images") MultipartFile[] images
    ) throws IOException {
        userProfileService.uploadPetGallery(petId, Arrays.asList(images));
        return ResponseEntity.ok("Images uploaded successfully");
    }
}

