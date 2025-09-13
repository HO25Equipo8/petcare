package com.petcare.back.controller;

import com.petcare.back.domain.dto.request.UserRegisterDTO;
import com.petcare.back.domain.dto.request.UserUpdateBackendDTO;
import com.petcare.back.domain.dto.request.UserUpdateDTO;
import com.petcare.back.domain.dto.response.BookingListDTO;
import com.petcare.back.domain.dto.response.UserDTO;
import com.petcare.back.domain.dto.response.UserUpdateResponseDTO;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.domain.mapper.response.UserUpdateResponseMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.UserRepository;
import com.petcare.back.service.BookingService;
import com.petcare.back.service.EmailService;
import com.petcare.back.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173/", allowedHeaders = "*", allowCredentials = "true")
@RestController
@RequestMapping("/user")
@SecurityRequirement(name = "bearer-key")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserUpdateResponseMapper userUpdateResponseMapper;
    @Autowired
    private EmailService emailService;
    @Autowired
    private BookingService bookingService;

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

        // mapear role profesional de el user


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
            description = "Permite actualizar datos personales y ubicación con autocompletado de google"
    )
    @PutMapping(value = "/update-profile-frontend", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfile(
            @RequestBody UserUpdateDTO dto,
            UriComponentsBuilder uriBuilder
    ) {
        try {

            User updatedUser = userService.updateProfile(dto);
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

    @Operation(
            summary = "Actualizar perfil del usuario autenticado para pruebas en el backend",
            description = "Permite actualizar datos personales y ubicación"
    )
    @PutMapping(value = "/update-profile-backend", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfile(
            @RequestBody UserUpdateBackendDTO dto,
            UriComponentsBuilder uriBuilder
    ) {
        try {

            User updatedUser = userService.updateProfileBackend(dto);
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

    @Operation(summary = "Obtener perfil del usuario autenticado")
    @GetMapping("/me-profile")
    public ResponseEntity<?> getAuthenticatedUserProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        UserUpdateResponseDTO responseDTO = userUpdateResponseMapper.toDTO(user);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", responseDTO
        ));
    }

    @Operation(
            summary = "Obtener reservas del usuario autenticado",
            description = "Devuelve todas las reservas asociadas al dueño o profesional autenticado"
    )
    @GetMapping("/my-bookings")
    public ResponseEntity<?> getMyBookings() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<BookingListDTO> bookings = bookingService.getBookingsForUser(user);
        return ResponseEntity.ok(Map.of("data", bookings));
    }

}