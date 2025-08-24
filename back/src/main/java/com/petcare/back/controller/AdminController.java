package com.petcare.back.controller;

import com.petcare.back.domain.dto.request.ComboOfferingCreateDTO;
import com.petcare.back.domain.dto.request.PlanCreateDTO;
import com.petcare.back.domain.dto.request.OfferingCreateDTO;
import com.petcare.back.domain.dto.response.ComboOfferingResponseDTO;
import com.petcare.back.domain.dto.response.PlanResponseDTO;
import com.petcare.back.domain.dto.response.OfferingResponseDTO;
import com.petcare.back.exception.MyException;
import com.petcare.back.service.ComboOfferingService;
import com.petcare.back.service.PlanService;
import com.petcare.back.service.OfferingService;
import com.petcare.back.service.ScheduleConfigService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true")
@RestController
@RequestMapping("/admin")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class AdminController {

    private final OfferingService offeringService;
    private final ComboOfferingService comboOfferingService;
    private final PlanService planService;
    private final ScheduleConfigService scheduleConfigService;

    @PostMapping("/register/service")
    public ResponseEntity<?> createService(@Valid @RequestBody OfferingCreateDTO dto, UriComponentsBuilder uriBuilder){
        try {
            OfferingResponseDTO service = offeringService.createService(dto);

            URI uri = uriBuilder.path("/services/{id}").buildAndExpand(service.id()).toUri();

            return ResponseEntity.created(uri).body(Map.of(
                    "status", "success",
                    "message", "Servicio registrado con éxito",
                    "data", service
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

    @PostMapping("/register/combo")
    public ResponseEntity<?> create(@RequestBody @Valid ComboOfferingCreateDTO dto, UriComponentsBuilder uriBuilder) {
        try {
            ComboOfferingResponseDTO combo = comboOfferingService.create(dto);

            URI uri = uriBuilder.path("/services/{id}").buildAndExpand(combo.id()).toUri();

            return ResponseEntity.created(uri).body(Map.of(
                    "status", "success",
                    "message", "Combo registrado con éxito",
                    "data", combo
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

    @PostMapping("/admin/schedules/expire-now")
    public ResponseEntity<String> expireNow() {
        int count = scheduleConfigService.expireOldSchedules();
        return ResponseEntity.ok("Se expiraron " + count + " horarios disponibles anteriores a hoy");
    }
}
