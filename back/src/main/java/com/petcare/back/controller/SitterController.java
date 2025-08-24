package com.petcare.back.controller;

import com.petcare.back.domain.dto.request.ScheduleConfigCreateDTO;
import com.petcare.back.domain.dto.response.ScheduleConfigResponseDTO;
import com.petcare.back.exception.MyException;
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
@RequestMapping("/sitter")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class SitterController {

    private final ScheduleConfigService scheduleConfigService;

    @PostMapping("/register/schedule")
    public ResponseEntity<?> create(@RequestBody @Valid ScheduleConfigCreateDTO dto, UriComponentsBuilder uriBuilder) {
        try {
            ScheduleConfigResponseDTO config = scheduleConfigService.createScheduleConfig(dto);

            URI uri = uriBuilder.path("/api/schedule-config/{id}").buildAndExpand(config.id()).toUri();

            return ResponseEntity.created(uri).body(Map.of(
                    "status", "success",
                    "message", "Configuración registrada y horarios generados con éxito",
                    "data", config
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
