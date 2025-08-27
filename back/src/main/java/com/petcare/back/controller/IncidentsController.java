package com.petcare.back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petcare.back.domain.dto.request.IncidentsDTO;
import com.petcare.back.domain.entity.IncidentImage;
import com.petcare.back.service.IncidentsService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class IncidentsController {


    private final IncidentsService incidentsService;



    public IncidentsController(IncidentsService incidentsService) {
        this.incidentsService = incidentsService;

    }

    @PostMapping("/incidents")
    public ResponseEntity<String> createIncidentsWithImages(
            @RequestPart("incident") String incidentJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) throws IOException {


        ObjectMapper mapper = new ObjectMapper();
        IncidentsDTO incidentsDTO = mapper.readValue(incidentJson, IncidentsDTO.class);


        incidentsService.createIncident(incidentsDTO, images);

        return ResponseEntity.ok("Incident created successfully");
    }

    @GetMapping("/incidents/{incidentId}/images/{imageId}")
    public ResponseEntity<byte[]> getImage(
            @PathVariable Long incidentId,
            @PathVariable Long imageId) {

        IncidentImage image = incidentsService.getIncidentImage(incidentId, imageId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getImageType()))
                .body(image.getData());
    }

    @GetMapping("/incidents/{incidentId}")
    public ResponseEntity<IncidentsDTO> getIncident(@PathVariable Long incidentId) {
        IncidentsDTO incidentsDTO = incidentsService.getIncidentsDTO(incidentId);
        return ResponseEntity.ok().body(incidentsDTO);
    }


}