package com.petcare.back.controller;

import com.petcare.back.domain.dto.request.PlanCreateDTO;
import com.petcare.back.domain.dto.request.UserPublicProfileDTO;
import com.petcare.back.domain.dto.response.PlanResponseDTO;
import com.petcare.back.domain.dto.response.UserUpdateResponseDTO;
import com.petcare.back.domain.entity.User;
import com.petcare.back.exception.MyException;
import com.petcare.back.service.PlanService;
import com.petcare.back.service.ScheduleConfigService;
import com.petcare.back.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true")
@RestController
@RequestMapping("/admin")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class AdminController {

    private final ScheduleConfigService scheduleConfigService;
    private final PlanService planService;
    private final UserService userService;

    //Metodo para cambiar el estado a expirado de las reservas anteriores al dia de hoy
    @Operation(
            summary = "Expirar horarios antiguos",
            description = "Este endpoint expira todos los horarios anteriores al día actual y elimina aquellos que no están vinculados a ninguna reserva. Solo accesible para usuarios con rol ADMIN."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/schedules/expire-now")
    public ResponseEntity<String> expireNow() {
        int count = scheduleConfigService.expireOldSchedules();
        int count1 = scheduleConfigService.deleteUnlinkedExpiredSchedules();
        return ResponseEntity.ok("Se expiraron " + count + " horarios y se eliminaron " + count1 + " sin reservas.");
    }

    @PostMapping("/register/plan")
    public ResponseEntity<?> create(@RequestBody @Valid PlanCreateDTO dto, UriComponentsBuilder uriBuilder) {
        try {
            PlanResponseDTO plan = planService.createPlan(dto);

            URI uri = uriBuilder.path("/api/plans/{id}").buildAndExpand(plan.id()).toUri();

            return ResponseEntity.created(uri).body(Map.of(
                    "status", "success",
                    "message", "Plan registrado con éxito",
                    "data", plan
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
            summary = "Listar planes",
            description = "Devuelve todos los planes disponibles en el sistema, con sus características, precios y condiciones."
    )
    @GetMapping("/list/plan")
    public ResponseEntity<?> getAll() {
        try {
            List<PlanResponseDTO> plans = planService.getAllPlans();

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Planes obtenidos con éxito",
                    "data", plans
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error al obtener los planes"
            ));
        }
    }

    @Operation(summary = "Obtener todos los perfiles públicos (solo admin)")
    @GetMapping("/public-profiles")
    public ResponseEntity<?> getAllPublicProfiles() {
        List<UserPublicProfileDTO> publicProfiles = userService.getAllPublicProfiles();

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", publicProfiles
        ));
    }

    @Operation(summary = "Obtener perfil completo por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserProfileById(@PathVariable Long id) {
        try {
            UserUpdateResponseDTO responseDTO = userService.getUserProfileById(id);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", responseDTO
            ));
        } catch (MyException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

}
