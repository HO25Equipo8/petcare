package com.petcare.back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petcare.back.domain.dto.request.IncidentsDTO;

import com.petcare.back.service.IncidentsService;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;


@RestController
@RequestMapping("/api")
public class IncidentsController {


    private final IncidentsService incidentsService;

    public IncidentsController(IncidentsService incidentsService) {
        this.incidentsService = incidentsService;
    }

    @PostMapping(value = "/incidents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Crear incidente con imagen", description = "Crea un incidente con los datos y una imagen asociada")
    public ResponseEntity<String> createIncidentsWithImage(
            @RequestPart("incident") String incidentJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        IncidentsDTO incidentsDTO = mapper.readValue(incidentJson, IncidentsDTO.class);

        incidentsService.createIncident(incidentsDTO, image);
        return ResponseEntity.ok("Incident created successfully");
    }

    @GetMapping("/incidents/{incidentId}")
    @Operation(summary = "Obtener incidente con DTO + imagen", description = "Devuelve el DTO de un incidente y su imagen asociada")
    public ResponseEntity<IncidentsDTO> getIncident(@PathVariable Long incidentId) {
        IncidentsDTO incidentsDTO = incidentsService.getIncidentsDTO(incidentId);
        return ResponseEntity.ok(incidentsDTO);
    }
}