package com.petcare.back.controller;

import com.petcare.back.domain.dto.request.StartSessionDTO;
import com.petcare.back.domain.dto.response.ServiceSessionResponseDTO;
import com.petcare.back.domain.entity.ServiceSession;
import com.petcare.back.domain.mapper.request.ServiceSessionMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.ServiceSessionRepository;
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

    private final UpdateSessionService sessionService;
    @Operation(
            summary = "Iniciar sesión de servicio",
            description = "Permite al cuidador (SITTER) iniciar una sesión asociada a una reserva activa. La sesión debe comenzar dentro del horario reservado."
    )
    @PostMapping("/{bookingId}/start")
    public ResponseEntity<Map<String, Object>> startSession(@PathVariable Long bookingId) throws MyException {
        ServiceSession session = sessionService.startSession(new StartSessionDTO(bookingId));
        ServiceSessionResponseDTO dto = ServiceSessionMapper.INSTANCE.toDto(session);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Sesión iniciada con éxito",
                "data", dto
        ));
    }
    @Operation(
            summary = "Unirse a sesión en curso",
            description = "Permite al dueño (OWNER) acceder a la sesión en vivo si está en progreso y la reserva le pertenece."
    )
    @GetMapping("/{bookingId}/join")
    public ResponseEntity<ServiceSessionResponseDTO> joinSession(@PathVariable Long bookingId) {
        ServiceSessionResponseDTO responseDTO = sessionService.joinSession(bookingId);
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(
            summary = "Finalizar sesión de servicio",
            description = "Permite al cuidador (SITTER) finalizar una sesión en curso. La sesión debe estar activa para poder cerrarse correctamente."
    )
    @PutMapping("/{id}/finish")
    public ResponseEntity<?> finishSession(@PathVariable Long id) throws MyException {
        ServiceSession session = sessionService.finish(id);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Sesión finalizada con éxito",
                "data", session
        ));
    }
}
