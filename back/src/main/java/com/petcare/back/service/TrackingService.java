package com.petcare.back.service;

import com.petcare.back.controller.WebSocketController;
import com.petcare.back.domain.dto.response.UpdateServiceNotificationDTO;
import com.petcare.back.domain.entity.TrackingPoint;
import com.petcare.back.repository.TrackingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TrackingService {

    private final TrackingRepository repository;
    private final WebSocketController webSocketController;

    @Transactional
    public TrackingPoint savePoint(Long sessionId, TrackingPoint point) {
        point.setSessionId(sessionId);
        point.setTimestamp(LocalDateTime.now());
        TrackingPoint saved = repository.save(point);

        // Crear notificación
        UpdateServiceNotificationDTO notification = new UpdateServiceNotificationDTO(
                sessionId,
                "📍 Ubicación actualizada",
                "Lat: " + saved.getLatitude() + ", Lng: " + saved.getLongitude(),
                null,
                saved.getTimestamp()
        );

        // Enviar por WebSocket
        webSocketController.notifyUpdate(sessionId, notification);

        return saved;
    }

    public TrackingPoint getLastLocation(Long sessionId) {
        return repository.findLastLocationBySessionId(sessionId);
    }
}