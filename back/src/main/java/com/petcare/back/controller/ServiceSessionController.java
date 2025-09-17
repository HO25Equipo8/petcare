package com.petcare.back.controller;

import com.petcare.back.domain.dto.request.StartSessionDTO;
import com.petcare.back.domain.dto.response.ServiceSessionResponseDTO;
import com.petcare.back.domain.entity.ServiceSession;
import com.petcare.back.domain.mapper.request.ServiceSessionMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.service.UpdateSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://127.0.0.1:5501", allowedHeaders = "*", allowCredentials = "true")
@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class ServiceSessionController {

    private final UpdateSessionService updateSessionService;

    @Operation(
            summary = "Iniciar sesión de servicio",
            description = "Permite al cuidador (SITTER) iniciar una sesión asociada a una reserva activa. La sesión debe comenzar dentro del horario reservado."
    )
    @PostMapping("/{bookingId}/start")
    public ResponseEntity<Map<String, Object>> startSession(@PathVariable Long bookingId) {
        try {
            ServiceSession session = updateSessionService.startSession(new StartSessionDTO(bookingId));
            ServiceSessionResponseDTO dto = ServiceSessionMapper.INSTANCE.toDto(session);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Sesión iniciada con éxito",
                    "data", dto
            ));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error interno del servidor: " + e.getMessage()
            ));
        }
    }
    @Operation(
            summary = "Unirse a sesión en curso",
            description = "Permite al dueño (OWNER) acceder a la sesión en vivo si está en progreso y la reserva le pertenece."
    )
    @GetMapping("/{bookingId}/join")
    public ResponseEntity<ServiceSessionResponseDTO> joinSession(@PathVariable Long bookingId) {
        ServiceSessionResponseDTO responseDTO = updateSessionService.joinSession(bookingId);
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(
            summary = "Finalizar sesión de servicio",
            description = "Permite al cuidador (SITTER) finalizar una sesión en curso. La sesión debe estar activa para poder cerrarse correctamente."
    )
    @PutMapping("/{sessionId}/finish")
    public ResponseEntity<?> finishSession(@PathVariable Long sessionId) throws MyException {
        ServiceSession session = updateSessionService.finish(sessionId);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Sesión finalizada con éxito",
                "data", ServiceSessionMapper.INSTANCE.toDto(session)
        ));
    }

    @Operation(
            summary = "Cancelar sesión",
            description = "Permite al cuidador (SITTER) cancelar una sesión en curso por motivos justificados. La sesión se marca como CANCELADA y se registra el motivo como actualización interna."
    )
    @PutMapping("/{sessionId}/cancel")
    public ResponseEntity<?> cancelSession(
            @PathVariable Long sessionId,
            @RequestParam String reason
    ) throws MyException {
        ServiceSession session = updateSessionService.cancelSession(sessionId, reason);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Sesión cancelada",
                "data", ServiceSessionMapper.INSTANCE.toDto(session)
        ));
    }

    @Operation(
            summary = "Postergar sesión",
            description = "Permite al cuidador (SITTER) postergar una sesión en curso por retrasos u otros motivos. La sesión se marca como POSTERGADA y se registra el motivo como actualización interna."
    )
    @PutMapping("/{sessionId}/postpone")
    public ResponseEntity<?> postponeSession(
            @PathVariable Long sessionId,
            @RequestParam String reason
    ) throws MyException {
        ServiceSession session = updateSessionService.postponeSession(sessionId, reason);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Sesión postergada",
                "data", ServiceSessionMapper.INSTANCE.toDto(session)
        ));
    }
}