package com.petcare.back.controller;

import com.petcare.back.domain.dto.request.UserRegisterDTO;
import com.petcare.back.domain.dto.request.UserUpdateBackendDTO;
import com.petcare.back.domain.dto.request.UserUpdateDTO;
import com.petcare.back.domain.dto.response.*;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.domain.enumerated.ScheduleStatus;
import com.petcare.back.domain.enumerated.WeekDayEnum;
import com.petcare.back.domain.mapper.response.UserUpdateResponseMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.UserRepository;
import com.petcare.back.service.BookingService;
import com.petcare.back.service.EmailService;
import com.petcare.back.service.ScheduleConfigService;
import com.petcare.back.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
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
    @Autowired
    private ScheduleConfigService scheduleConfigService;
    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

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

        newUser.setChecked(true);
        newUser.setActive(true);

        userRepository.save(newUser);

//        // Send email notifications
        try {
            emailService.sendWelcomeEmail(newUser.getEmail(), getUserName(newUser));
            emailService.sendAdminNotification(newUser.getEmail(), getUserName(newUser));
        } catch (Exception e) {
            // If email fails, return error (as requested)
            log.error("Usuario creado pero error enviando emails: " + e.getMessage());
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

    @Operation(
            summary = "Listar horarios filtrados",
            description = """
                    Devuelve la lista de horarios (`Schedule`) según los filtros opcionales enviados.
                    Este endpoint está disponible para usuarios con rol `SITTER`, `OWNER` o `ADMIN`, y adapta la lógica según el rol autenticado.

                    - Si no se envía ningún filtro, devuelve todos los horarios accesibles para el usuario.
                    - Si se envía `status`, filtra por estado del horario (DISPONIBLE, RESERVADO, etc.).
                    - Si se envía `day`, filtra por día de la semana.
                    - Si se envía `configId`, filtra por configuración horaria origen.
                    - Si se envía `from` y `to`, filtra por rango de fechas.
                    - Los filtros pueden combinarse para obtener resultados más precisos.

                    Ejemplos de uso:
                    - `/schedule/list?status=DISPONIBLE`
                    - `/schedule/list?day=LUNES&status=RESERVADO`
                    - `/schedule/list?configId=5&from=2025-09-01&to=2025-09-30`
                    """
    )
    @GetMapping("/schedule/list")
    public ResponseEntity<Map<String, Object>> getFilteredSchedules(@RequestParam(required = false) ScheduleStatus status,
                                                                    @RequestParam(required = false) WeekDayEnum day,
                                                                    @RequestParam(required = false) Long configId,
                                                                    @RequestParam(required = false) LocalDate from,
                                                                    @RequestParam(required = false) LocalDate to,
                                                                    @AuthenticationPrincipal User user,
                                                                    @PageableDefault(size = 10, sort = "establishedTime", direction = Sort.Direction.ASC) Pageable pageable) {
        try {
            Map<String, Object> response;

            switch (user.getRole()) {
                case SITTER -> {
                    Page<ScheduleResponseDTO> result = scheduleConfigService.getFilteredSchedulesForSitter(user, status, day, configId, from, to, pageable);
                    response = Map.of(
                            "status", "success",
                            "data", result.getContent(),
                            "page", result.getNumber(),
                            "size", result.getSize(),
                            "totalPages", result.getTotalPages(),
                            "totalElements", result.getTotalElements()
                    );
                }
                case OWNER -> {
                    Page<ScheduleWithSitterDTO> result = scheduleConfigService.getFilteredSchedulesForOwner(user, status, day, from, to, pageable);
                    response = Map.of(
                            "status", "success",
                            "data", result.getContent(),
                            "page", result.getNumber(),
                            "size", result.getSize(),
                            "totalPages", result.getTotalPages(),
                            "totalElements", result.getTotalElements()
                    );
                }
                case ADMIN -> {
                    Page<ScheduleWithSitterDTO> result = scheduleConfigService.getFilteredSchedulesForAdmin(status, day, configId, from, to, pageable);
                    response = Map.of(
                            "status", "success",
                            "data", result.getContent(),
                            "page", result.getNumber(),
                            "size", result.getSize(),
                            "totalPages", result.getTotalPages(),
                            "totalElements", result.getTotalElements()
                    );
                }
                default -> throw new MyException("Rol no autorizado para consultar horarios");
            }

            return ResponseEntity.ok(response);

        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<?> activateUser(@PathVariable Long id) {
        try {
            User user = userService.activateUser(id);
            return ResponseEntity.ok("Usuario " + user.getId() + " activado correctamente");
        } catch (MyException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateUser(@PathVariable Long id) {
        try {
            User user = userService.deactivateUser(id);
            return ResponseEntity.ok("Usuario " + user.getId() + " desactivado correctamente");
        } catch (MyException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}