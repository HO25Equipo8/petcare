package com.petcare.back.controller;

import com.petcare.back.domain.dto.request.IncidentsDTO;
import com.petcare.back.domain.dto.request.UpdateServiceRequestDTO;
import com.petcare.back.domain.dto.response.UpdateServiceNotificationDTO;
import com.petcare.back.domain.dto.response.UpdateServiceResponseDTO;
import com.petcare.back.domain.entity.ServiceSession;
import com.petcare.back.domain.entity.UpdateService;
import com.petcare.back.domain.enumerated.IncidentsTypes;
import com.petcare.back.domain.mapper.response.UpdateServiceResponseMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.service.UpdateSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

@CrossOrigin(origins = "http://127.0.0.1:5501", allowedHeaders = "*", allowCredentials = "true")
@RestController
@RequestMapping("/sessions/{sessionId}/updates")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class UpdateServiceController {

    private final UpdateSessionService updateSessionService;
    private final UpdateServiceResponseMapper responseMapper;
    private final WebSocketController webSocketController;

    @Operation(
            summary = "Registrar actualización de sesión",
            description = "Permite al cuidador (SITTER) enviar una actualización con título, mensaje y archivo opcional durante una sesión en curso. La actualización se notifica en tiempo real al dueño."
    )
    @PostMapping
    public ResponseEntity<?> addUpdate(
            @PathVariable Long sessionId,
            @RequestParam("title") String title,
            @RequestParam("message") String message,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) throws IOException, MyException {

        UpdateServiceRequestDTO dto = new UpdateServiceRequestDTO(title, message, file);
        UpdateService update = updateSessionService.addUpdate(sessionId, dto);

        UpdateServiceResponseDTO responseDTO = responseMapper.toDto(update);

        UpdateServiceNotificationDTO notification = new UpdateServiceNotificationDTO(
                sessionId,
                update.getTitle(),
                update.getMessage(),
                update.getImage() != null ? "/api/images/" + update.getImage().getId() : null,
                LocalDateTime.now()
        );

        webSocketController.notifyUpdate(sessionId, notification);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Actualización registrada",
                "data", responseDTO
        ));
    }
    @Operation(
            summary = "Reportar incidente en sesión",
            description = "Permite al cuidador (SITTER) registrar un incidente ocurrido durante la sesión. El tipo y la descripción del incidente se vinculan a la reserva correspondiente."
    )
    @PostMapping("/incident")
    public ResponseEntity<?> reportIncident(
            @PathVariable Long sessionId,
            @RequestParam("type") IncidentsTypes type,
            @RequestParam("description") String description
    ) throws MyException {

        ServiceSession session = updateSessionService.getSession(sessionId);
        Long bookingId = session.getBooking().getId();

        IncidentsDTO dto = new IncidentsDTO(type, description, Instant.now(), bookingId);

        Long incidentId = updateSessionService.reportIncident(session, dto);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Incidente registrado",
                "incidentId", incidentId
        ));
    }
}