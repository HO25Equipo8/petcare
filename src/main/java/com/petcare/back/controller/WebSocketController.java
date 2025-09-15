package com.petcare.back.controller;

import com.petcare.back.domain.dto.response.ServiceSessionResponseDTO;
import com.petcare.back.domain.dto.response.UpdateServiceNotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://127.0.0.1:5501", allowedHeaders = "*", allowCredentials = "true")
@RestController
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    // Notificar nueva actualización
    public void notifyUpdate(Long sessionId, UpdateServiceNotificationDTO dto) {
        messagingTemplate.convertAndSend("/topic/sessions/" + sessionId, dto);
    }

    // Notificar inicio de sesión
    public void notifySessionStarted(ServiceSessionResponseDTO dto) {
        messagingTemplate.convertAndSend("/topic/sessions", dto);
    }
}

