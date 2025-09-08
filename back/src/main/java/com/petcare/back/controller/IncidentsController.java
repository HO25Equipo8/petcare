package com.petcare.back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petcare.back.domain.dto.request.IncidentsDTO;

import com.petcare.back.domain.dto.response.IncidentsResponseDTO;
import com.petcare.back.domain.entity.Image;
import com.petcare.back.service.IncidentsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@SecurityRequirement(name = "bearer-key")
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class IncidentsController {


    private final IncidentsService incidentsService;

    // 1️⃣ Crear incidente (sin imágenes todavía)
    @PostMapping
    @Operation(summary = "Crear incidente", description = "Crea un incidente sin imágenes")
    public ResponseEntity<Long> createIncident(@RequestBody IncidentsDTO incidentsDTO) throws IOException {
        Long incidentId = incidentsService.createIncident(incidentsDTO);
        return ResponseEntity.ok(incidentId);
    }

    @PostMapping(
            path = "/{incidentId}/images",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(
            summary = "Subir imágenes de un incidente",
            description = "Carga hasta 3 imágenes para un incidente existente"
    )
    public ResponseEntity<String> uploadImages(
            @PathVariable Long incidentId,
            @Parameter(
                    description = "Imágenes a subir (máx 3)",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            @RequestPart("images") MultipartFile[] images
    ) throws IOException {
        incidentsService.addImagesToIncident(incidentId, Arrays.asList(images));
        return ResponseEntity.ok("Images uploaded successfully");
    }

    // 3️⃣ Obtener incidente (solo datos del incidente)
    @GetMapping("/{incidentId}")
    @Operation(summary = "Obtener incidente", description = "Devuelve los datos de un incidente sin imágenes")
    public ResponseEntity<IncidentsResponseDTO> getIncident(@PathVariable Long incidentId) {
        IncidentsResponseDTO incidentsResponseDTO = incidentsService.getIncidentsDTO(incidentId);
        return ResponseEntity.ok(incidentsResponseDTO);
    }

    // 4️⃣ Obtener imágenes del incidente
    @GetMapping("/{incidentId}/images")
    @Operation(summary = "Obtener imágenes de un incidente", description = "Devuelve las imágenes asociadas a un incidente")
    public ResponseEntity<List<Image>> getIncidentImages(@PathVariable Long incidentId) {
        List<Image> images = incidentsService.getIncidentImages(incidentId);
        return ResponseEntity.ok(images);
    }
}