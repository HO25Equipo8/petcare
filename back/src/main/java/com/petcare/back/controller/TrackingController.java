package com.petcare.back.controller;

import com.petcare.back.domain.dto.request.TrackingPointNotificationDTO;
import com.petcare.back.domain.dto.request.TrackingPointRequestDTO;
import com.petcare.back.exception.MyException;
import com.petcare.back.service.UpdateSessionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/tracking")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:5501", allowedHeaders = "*", allowCredentials = "true")
public class TrackingController {

    private final UpdateSessionService updateSessionService;

    @Operation(
            summary = "Registrar punto de ubicación en sesión",
            description = "Permite al cuidador (SITTER) enviar una coordenada geográfica durante el paseo. La ubicación se transmite en tiempo real al dueño y puede ser almacenada como historial del recorrido."
    )
    @PostMapping("/{sessionId}")
    public ResponseEntity<?> addTracking(
            @PathVariable Long sessionId,
            @RequestBody TrackingPointRequestDTO dto
    ) throws MyException {
        updateSessionService.addTrackingPoint(sessionId, dto.lat(), dto.lng());
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Ubicación enviada"
        ));
    }
    @Operation(
            summary = "Obtener última ubicación registrada",
            description = "Devuelve el último punto de ubicación enviado durante la sesión. Útil para visualizar el estado actual del paseo o reconstruir el recorrido en tiempo real."
    )
    @GetMapping("/{sessionId}/last")
    public ResponseEntity<TrackingPointNotificationDTO> getLastLocation(
            @PathVariable Long sessionId
    ) throws MyException {
        TrackingPointNotificationDTO lastPoint = updateSessionService.getLastTrackingPoint(sessionId);
        return ResponseEntity.ok(lastPoint);
    }
}