package com.petcare.back.controller;

import com.petcare.back.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public ResponseEntity<String> uploadProfilePhoto(@RequestParam("file") MultipartFile file) {
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
}

