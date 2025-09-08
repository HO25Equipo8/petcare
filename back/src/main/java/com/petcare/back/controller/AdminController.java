package com.petcare.back.controller;

import com.petcare.back.domain.dto.response.PlanResponseDTO;
import com.petcare.back.service.PlanService;
import com.petcare.back.service.ScheduleConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true")
@RestController
@RequestMapping("/admin")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class AdminController {

    private final ScheduleConfigService scheduleConfigService;
    private final PlanService planService;

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
}
