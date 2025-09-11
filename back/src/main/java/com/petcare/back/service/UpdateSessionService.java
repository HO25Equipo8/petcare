package com.petcare.back.service;

import com.petcare.back.controller.WebSocketController;
import com.petcare.back.domain.dto.request.IncidentsDTO;
import com.petcare.back.domain.dto.request.StartSessionDTO;
import com.petcare.back.domain.dto.request.UpdateServiceRequestDTO;
import com.petcare.back.domain.dto.response.IncidentSessionResponseDTO;
import com.petcare.back.domain.dto.response.IncidentsResponseDTO;
import com.petcare.back.domain.dto.response.ServiceSessionResponseDTO;
import com.petcare.back.domain.dto.response.UpdateServiceNotificationDTO;
import com.petcare.back.domain.entity.*;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.domain.enumerated.ServiceSessionStatus;
import com.petcare.back.domain.mapper.request.ServiceSessionMapper;
import com.petcare.back.domain.mapper.request.UpdateServiceCreateMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdateSessionService {

    private final ServiceSessionRepository sessionRepository;
    private final UpdateServiceRepository updateRepository;
    private final ImageRepository imageRepository;
    private final BookingRepository bookingRepository;
    private final IncidentServiceImpl image;
    private final WebSocketController webSocketController;
    private final IncidentsRepository incidentsRepository;
    private final IncidentsTableRepository incidentsTableRepository;
    private final ServiceSessionMapper serviceSessionMapper;

    @Transactional
    public UpdateService addUpdate(Long sessionId, UpdateServiceRequestDTO dto) throws IOException, MyException {
        ServiceSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session no encontrada"));

        User user = getAuthenticatedUser();

        if (user.getRole() != Role.SITTER) {
            throw new MyException("Solo el profesional puede actualizar la sesión");
        }

        // Crear el update
        UpdateService update = new UpdateService();
        update.setTitle(dto.title());
        update.setMessage(dto.message());
        update.setServiceSession(session);

        // Guardar la imagen si existe
        if (dto.file() != null && !dto.file().isEmpty()) {
            Image img = image.processImage(dto.file()); // guarda/comprime la imagen
            imageRepository.save(img);
            update.setImage(img); // asociar la imagen al update
        }

        session.getUpdates().add(update);
        return updateRepository.save(update);
    }

    @Transactional
    public ServiceSession startSession(StartSessionDTO dto) throws MyException {

        User user = getAuthenticatedUser();

        if (user.getRole() != Role.SITTER) {
            throw new MyException("Solo el profesional puede iniciar la sesión");
        }

        Booking booking = bookingRepository.findById(dto.bookingId())
                .orElseThrow(() -> new RuntimeException("Booking no encontrado"));

        // Si ya existe, la eliminamos (o finalizamos)
        sessionRepository.findByBookingId(booking.getId())
                .ifPresent(existing -> sessionRepository.delete(existing));

        ServiceSession session = new ServiceSession();
        session.setBooking(booking);
        session.setStatus(ServiceSessionStatus.EN_PROGRESO);
        session.setStartTime(LocalDateTime.now());

        return sessionRepository.save(session);
    }

    @Transactional
    public ServiceSession finish(Long sessionId) throws MyException {

        ServiceSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session no encontrada"));

        User user = getAuthenticatedUser();

        if (user.getRole() != Role.SITTER) {
            throw new MyException("Solo el profesional puede finalizar la sesión");
        }
        session.setStatus(ServiceSessionStatus.FINALIZADO);
        session.setEndTime(LocalDateTime.now());

        return sessionRepository.save(session);
    }

    @Transactional
    public Long reportIncident(ServiceSession session, IncidentsDTO dto) throws MyException {

        User user = getAuthenticatedUser();

        if (user.getRole() != Role.SITTER) {
            throw new MyException("Solo el profesional puede reportar incidentes dentro de la sesión");
        }

        // 1️⃣ Crear incidente
        Incidents incident = new Incidents();
        incident.setIncidentsType(dto.getIncidentsType());
        incident.setDescription(dto.getDescription());
        incident.setBooking(session.getBooking());
        incident.setIncidentsDate(dto.getIncidentsDate());
        incident.setServiceSession(session);

        incidentsRepository.save(incident);

        // 2️⃣ Obtener datos para notificar
        IncidentSessionResponseDTO response = getIncidentsDTO(incident.getId());

        // 3️⃣ Notificar por WebSocket
        UpdateServiceNotificationDTO notification = new UpdateServiceNotificationDTO(
                session.getId(),
                "⚠️ Incidente: " + response.incidentsType().name().replace("_", " "),
                response.description(),
                null,
                response.incidentsDate().atZone(ZoneId.systemDefault()).toLocalDateTime()
        );
        webSocketController.notifyUpdate(session.getId(), notification);

        return incident.getId();
    }
    public ServiceSession getSession(Long sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session no encontrada"));
    }

    public IncidentSessionResponseDTO getIncidentsDTO(Long incidentId) {
        return incidentsTableRepository.findById(incidentId)
                .map(table -> mapFromTable(table))
                .orElseGet(() -> {
                    Incidents incident = incidentsRepository.findById(incidentId)
                            .orElseThrow(() -> new RuntimeException("Incidente no encontrado"));
                    return mapToSessionDTO(incident);
                });
    }
    private IncidentSessionResponseDTO mapToSessionDTO(Incidents incident) {
        return new IncidentSessionResponseDTO(
                incident.getId(),
                incident.getIncidentsType(),
                incident.getDescription(),
                incident.getIncidentsDate(),
                incident.getServiceSession().getId(),
                incident.getBooking().getId(),
                incident.getBooking().getPet().getName(),
                incident.getBooking().getOwner().getName()
        );
    }
    private IncidentSessionResponseDTO mapFromTable(IncidentsTable table) {
        return new IncidentSessionResponseDTO(
                table.getIncidentId(),
                null,
                "Incidente registrado (sin detalles)",
                Instant.now(),
                null,
                null,
                null,
                null
        );
    }
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return user;
    }

    public ServiceSessionResponseDTO joinSession(Long bookingId) {
        ServiceSession session = sessionRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("No hay sesión activa para este booking"));

        ServiceSessionResponseDTO responseDTO = serviceSessionMapper.toDto(session);
        return responseDTO;
    }
}
