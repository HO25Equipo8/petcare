package com.petcare.back.controller;

import com.petcare.back.domain.entity.TrackingPoint;
import com.petcare.back.service.TrackingService;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/tracking")
public class TrackingController {

    private final TrackingService service;

    public TrackingController(TrackingService service) {
        this.service = service;
    }


    @PostMapping("/{bookingId}")
    public TrackingPoint saveLocation(@PathVariable UUID bookingId, @RequestBody TrackingPoint point) {
        point.setBookingId(bookingId);
        point.setTimestamp(java.time.LocalDateTime.now());
        return service.savePoint(point);
    }

    // Obtener última ubicación
    @GetMapping("/{bookingId}/last")
    public TrackingPoint getLastLocation(@PathVariable UUID bookingId) {
        return service.getLastLocation(bookingId);
    }
}

