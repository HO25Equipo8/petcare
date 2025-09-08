package com.petcare.back.controller;

import com.petcare.back.domain.entity.TrackingPoint;
import com.petcare.back.service.TrackingService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/tracking")
@RequiredArgsConstructor
public class TrackingController {

    private final TrackingService service;

    @Operation(
            summary = "Registrar ubicación en tiempo real",
            description = "Permite guardar un punto de ubicación asociado a una reserva específica. Se registra la latitud, longitud y el timestamp actual. Útil para seguimiento de paseos o servicios en curso."
    )
    @PostMapping("/{bookingId}")
    public TrackingPoint saveLocation(@PathVariable UUID bookingId, @RequestBody TrackingPoint point) {
        point.setBookingId(bookingId);
        point.setTimestamp(java.time.LocalDateTime.now());
        return service.savePoint(point);
    }

    // Obtener última ubicación
    @Operation(
            summary = "Consultar última ubicación registrada",
            description = "Devuelve el último punto de ubicación registrado para una reserva específica. Útil para visualizar el estado actual del servicio en tiempo real."
    )
    @GetMapping("/{bookingId}/last")
    public TrackingPoint getLastLocation(@PathVariable UUID bookingId) {
        return service.getLastLocation(bookingId);
    }
}

