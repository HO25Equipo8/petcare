package com.petcare.back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petcare.back.domain.dto.request.UserRegisterDTO;
import com.petcare.back.domain.dto.request.UserUpdateDTO;
import com.petcare.back.domain.dto.response.UserDTO;
import com.petcare.back.domain.dto.response.UserUpdateResponseDTO;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.domain.mapper.response.UserUpdateResponseMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.UserRepository;
import com.petcare.back.service.EmailService;
import com.petcare.back.service.LocationService;
import com.petcare.back.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/user")
@SecurityRequirement(name = "bearer-key")
public class RegisterController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LocationService locationService;
    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserUpdateResponseMapper userUpdateResponseMapper;

    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody @Valid UserRegisterDTO userRegisterDTO
            , UriComponentsBuilder uriComponentsBuilder){
        // Check for empty email
        if (userRegisterDTO.login() == null) {
            return ResponseEntity.badRequest().body("Email no puede estar vacío");
        }
        // Check if email already exists
        if (userRepository.findByEmail(userRegisterDTO.login()) != null) {
            return ResponseEntity.badRequest().body("Email ya existe");
        }
        //Check for empty passwords
        if (userRegisterDTO.pass1() == null || userRegisterDTO.pass2() == null) {
            return ResponseEntity.badRequest().body("Contraseñas no pueden estar vacías");
        }

        // Check if passwords match
        if (!userRegisterDTO.pass1().equals(userRegisterDTO.pass2())) {
            return ResponseEntity.badRequest().body("Contraseñas no coinciden");
        }

        // Check if passwords match
        if (!userRegisterDTO.pass1().equals(userRegisterDTO.pass2())) {
            return ResponseEntity.badRequest().body("Passwords do not match");
        }

        // Determinar el rol general
        Role role = (userRegisterDTO.role() != null) ? userRegisterDTO.role() : Role.USER;

        // Encriptar la contraseña
        String encryptedPassword = passwordEncoder.encode(userRegisterDTO.pass1());

        // Crear el usuario con el nuevo constructor
        User newUser = new User(
                userRegisterDTO.login(),
                encryptedPassword,
                role);

        if (userRegisterDTO.role() == Role.ADMIN || userRegisterDTO.role() == Role.OWNER) {
            newUser.setVerified(true);
        }

        userRepository.save(newUser);

        // Send email notifications
        try {
            // Send welcome email to user
            emailService.sendWelcomeEmail(newUser.getEmail(), getUserName(newUser));

            // Send notification to admin
            emailService.sendAdminNotification(newUser.getEmail(), getUserName(newUser));

        } catch (Exception e) {
            // If email fails, return error (as requested)
            return ResponseEntity.status(500).body("Usuario creado pero error enviando emails: " + e.getMessage());
        }

        // Respuesta con DTO
        UserDTO userDTO = new UserDTO(newUser.getId());
        URI url = uriComponentsBuilder.path("/users/{id}")
                .buildAndExpand(newUser.getId())
                .toUri();

        return ResponseEntity.created(url).body(userDTO);
    }

    @Operation(
            summary = "Actualizar perfil del usuario autenticado",
            description = "Permite actualizar datos personales, ubicación, foto de perfil y fotos de verificación (solo para SITTER)"
    )
    @PutMapping(value = "/update-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProfile(
            @Parameter(
                    description = "Datos del usuario en formato JSON",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserUpdateDTO.class)
                    )
            )
            @RequestPart("data") String rawJson,   // 👈 recibimos string plano

            @Parameter(
                    description = "Fotos de verificación de identidad (solo para SITTER)",
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                            schema = @Schema(type = "string", format = "binary"))
            )
            @RequestPart(value = "images", required = false) MultipartFile[] identityPhotos,

            @Parameter(
                    description = "Foto de perfil",
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                            schema = @Schema(type = "string", format = "binary"))
            )
            @RequestPart(value = "profilePhoto", required = false) MultipartFile profilePhoto,

            UriComponentsBuilder uriBuilder
    ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            UserUpdateDTO dto = mapper.readValue(rawJson, UserUpdateDTO.class);

            User updatedUser = userService.updateProfile(dto, profilePhoto, identityPhotos);

            URI uri = uriBuilder.path("/api/users/{id}")
                    .buildAndExpand(updatedUser.getId())
                    .toUri();

            UserUpdateResponseDTO responseDTO = userUpdateResponseMapper.toDTO(updatedUser);

            return ResponseEntity.created(uri).body(Map.of(
                    "status", "success",
                    "message", "Perfil actualizado con éxito",
                    "data", responseDTO
            ));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error interno del servidor"
            ));
        }
    }
    // Helper method to get user name
    private String getUserName(User user) {
        return user.getEmail().split("@")[0]; // Uses part before @ as name
    }
}