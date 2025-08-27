package com.petcare.back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petcare.back.domain.dto.request.IncidentsDTO;
import com.petcare.back.domain.entity.Image;
import com.petcare.back.service.IncidentsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @PostMapping("/incidents")
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
    public ResponseEntity<IncidentsDTO> getIncident(@PathVariable Long incidentId) {
        IncidentsDTO incidentsDTO = incidentsService.getIncidentsDTO(incidentId);
        return ResponseEntity.ok().body(incidentsDTO);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Subir una imagen", description = "Permite cargar una imagen al servidor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagen subida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Image.class))),
            @ApiResponse(responseCode = "400", description = "Error al subir la imagen")
    })
    public ResponseEntity<Image> upload(
            @Parameter(description = "Archivo de imagen a subir", required = true)
            @RequestParam("file") MultipartFile file) {
        try {
            Image savedImage = incidentsService.uploadImage(file);
            return ResponseEntity.ok(savedImage);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }
}