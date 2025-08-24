package com.petcare.back.controller;

import com.petcare.back.domain.dto.request.ComboServiceCreateDTO;
import com.petcare.back.domain.dto.request.PlanCreateDTO;
import com.petcare.back.domain.dto.request.ServiceCreateDTO;
import com.petcare.back.domain.dto.response.ComboServiceResponseDTO;
import com.petcare.back.domain.dto.response.PlanResponseDTO;
import com.petcare.back.domain.dto.response.ServiceResponseDTO;
import com.petcare.back.exception.MyException;
import com.petcare.back.service.ComboServiceService;
import com.petcare.back.service.PlanService;
import com.petcare.back.service.ServiceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true")
@RestController
@RequestMapping("/admin")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class AdminController {

    private final ServiceService serviceService;
    private final ComboServiceService comboServiceService;
    private final PlanService planService;

    @PostMapping("/register/service")
    public ResponseEntity<?> createService(@Valid @RequestBody ServiceCreateDTO dto,  UriComponentsBuilder uriBuilder){
        try {
            ServiceResponseDTO service = serviceService.createService(dto);

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
    public ResponseEntity<?> create(@RequestBody @Valid ComboServiceCreateDTO dto, UriComponentsBuilder uriBuilder) {
        try {
            ComboServiceResponseDTO combo = comboServiceService.create(dto);

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
}
