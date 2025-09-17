package com.petcare.back.service;

import com.petcare.back.controller.WebSocketController;
import com.petcare.back.domain.dto.request.IncidentsDTO;
import com.petcare.back.domain.dto.request.StartSessionDTO;
import com.petcare.back.domain.dto.request.TrackingPointNotificationDTO;
import com.petcare.back.domain.dto.request.UpdateServiceRequestDTO;
import com.petcare.back.domain.dto.response.*;
import com.petcare.back.domain.entity.*;
import com.petcare.back.domain.enumerated.BookingStatusEnum;
import com.petcare.back.domain.enumerated.IncidentsTypes;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.domain.enumerated.ServiceSessionStatus;
import com.petcare.back.domain.mapper.request.ServiceSessionMapper;
import com.petcare.back.domain.mapper.request.UpdateServiceCreateMapper;
import com.petcare.back.domain.mapper.response.UpdateServiceResponseMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.*;
import com.petcare.back.validation.ValidationBooking;
import com.petcare.back.validation.ValidationSessionServices;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UpdateSessionService {

    private final ServiceSessionRepository sessionRepository;
    private final UpdateServiceRepository updateRepository;
    private final ImageRepository imageRepository;
    private final BookingRepository bookingRepository;
    private final IncidentServiceImpl incidentService;
    private final WebSocketController webSocketController;
    private final IncidentsRepository incidentsRepository;
    private final UpdateServiceResponseMapper serviceSessionMapper;
    private final List<ValidationSessionServices> validations;
    private final TrackingRepository trackingRepository;

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
                .ifPresent(sessionRepository::delete);

        ServiceSession session = new ServiceSession();
        session.setBooking(booking);
        session.setStatus(ServiceSessionStatus.EN_PROGRESO);
        session.setStartTime(LocalDateTime.now());

        for (ValidationSessionServices v : validations) {
            v.validate(user, session);
        }

        return sessionRepository.save(session);
    }

    @Transactional
    public UpdateService addUpdate(Long sessionId, UpdateServiceRequestDTO dto) throws IOException, MyException {
        ServiceSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session no encontrada"));

        User user = getAuthenticatedUser();

        if (user.getRole() != Role.SITTER) {
            throw new MyException("Solo el profesional puede actualizar la sesión");
        }

        for (ValidationSessionServices v : validations) {
            v.validate(user, session);
        }

        // Crear el update
        UpdateService update = new UpdateService();
        update.setTitle(dto.title());
        update.setMessage(dto.message());
        update.setServiceSession(session);

        // Guardar la imagen si existe
        if (dto.file() != null && !dto.file().isEmpty()) {
            Image img = incidentService.processImage(dto.file()); // guarda/comprime la imagen
            imageRepository.save(img);
            update.setImage(img); // asociar la imagen al update
        }

        session.getUpdates().add(update);
        return updateRepository.save(update);
    }



    @Transactional
    public ServiceSession postponeSession(Long sessionId, String reason) throws MyException {
        ServiceSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));

        User user = getAuthenticatedUser();
        if (user.getRole() != Role.SITTER) {
            throw new MyException("Solo el profesional puede postergar la sesión");
        }

        if (session.getStatus() != ServiceSessionStatus.EN_PROGRESO) {
            throw new MyException("Solo se puede postergar una sesión en progreso");
        }

        session.setStatus(ServiceSessionStatus.POSTERGADO);
        session.setEndTime(LocalDateTime.now());

        UpdateService update = new UpdateService();
        update.setTitle("Sesión postergada");
        update.setMessage(reason);
        update.setServiceSession(session);
        updateRepository.save(update);

        return sessionRepository.save(session);
    }

    @Transactional
    public ServiceSession finish(Long sessionId) throws MyException {
        System.out.println("👉 Entrando en FINISH con sessionId=" + sessionId);
        ServiceSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session no encontrada"));

        User user = getAuthenticatedUser();

        if (user.getRole() != Role.SITTER) {
            throw new MyException("Solo el profesional puede finalizar la sesión");
        }

        if (session.getStatus() == ServiceSessionStatus.FINALIZADO) {
            throw new MyException("La sesión ya fue finalizada, no se pueden enviar actualizaciones");
        }

        session.setStatus(ServiceSessionStatus.FINALIZADO);
        session.setEndTime(LocalDateTime.now());

        for (ValidationSessionServices v : validations) {
            v.validate(user, session);
        }

        Booking booking = bookingRepository.getReferenceById(session.getBooking().getId());
        booking.setStatus(BookingStatusEnum.COMPLETADO);
        bookingRepository.save(booking);

        return sessionRepository.save(session);
    }
    @Transactional
    public ServiceSession cancelSession(Long sessionId, String reason) throws MyException {
        System.out.println("👉 Entrando en CANCEL con sessionId=" + sessionId);
        ServiceSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));

        User user = getAuthenticatedUser();
        if (user.getRole() != Role.SITTER) {
            throw new MyException("Solo el profesional puede cancelar la sesión");
        }

        if (session.getStatus() == ServiceSessionStatus.FINALIZADO) {
            throw new MyException("No se puede cancelar una sesión ya finalizada");
        }

        session.setStatus(ServiceSessionStatus.CANCELADO);
        session.setEndTime(LocalDateTime.now());

        UpdateService update = new UpdateService();
        update.setTitle("Sesión cancelada");
        update.setMessage(reason);
        update.setServiceSession(session);
        updateRepository.save(update);

        return sessionRepository.save(session);
    }

    @Transactional
    public Long reportIncident(ServiceSession session, IncidentsDTO dto) throws MyException {

        User user = getAuthenticatedUser();

        if (user.getRole() != Role.SITTER) {
            throw new MyException("Solo el profesional puede reportar incidentes dentro de la sesión");
        }

        // 1️⃣ Crear incidente
        Long incidentId = incidentService.createIncident(dto);
        Incidents incident = incidentsRepository.getReferenceById(incidentId);

        incident.setServiceSession(session);

        incidentsRepository.save(incident);

        // 2️⃣ Obtener datos para notificar
        IncidentsDTO response = incidentService.getIncidentsDTO(incident.getId());

        // 3️⃣ Notificar por WebSocket
        UpdateServiceNotificationDTO notification = new UpdateServiceNotificationDTO(
                session.getId(),
                "⚠️ Incidente: " + response.getIncidentsType().name().replace("_", " "),
                response.getDescription(),
                null,
                response.getIncidentsDate().atZone(ZoneId.systemDefault()).toLocalDateTime()
        );
        webSocketController.notifyUpdate(session.getId(), notification);

        return incident.getId();
    }
    public ServiceSession getSession(Long sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session no encontrada"));
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return user;
    }

    public ServiceSessionResponseDTO joinSession(Long bookingId) {
        ServiceSession session = sessionRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("No hay sesión activa para este booking"));

        List<UpdateService> updates = updateRepository.findByServiceSession_IdOrderByIdAsc(session.getId());
        List<UpdateServiceResponseDTO> updateDTOs = updates.stream()
                .map(serviceSessionMapper::toDto)
                .collect(Collectors.toList());

        return new ServiceSessionResponseDTO(
                session.getId(),
                session.getBooking().getId(),
                session.getStatus(),
                session.getStartTime(),
                updateDTOs
        );
    }

    @Transactional
    public void addTrackingPoint(Long sessionId, Double lat, Double lng) throws MyException {
        ServiceSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session no encontrada"));

        TrackingPoint point = new TrackingPoint(null, lat, lng, LocalDateTime.now(), sessionId);
        trackingRepository.save(point);

        UpdateServiceNotificationDTO notification = new UpdateServiceNotificationDTO(
                session.getId(),
                "Ubicación actual",
                lat + "," + lng,
                null,
                LocalDateTime.now()
        );
        webSocketController.notifyUpdate(session.getId(), notification);
    }

    public TrackingPointNotificationDTO getLastTrackingPoint(Long sessionId) {
        // Buscar el último tracking point
        TrackingPoint lastPoint = trackingRepository
                .findBySessionIdOrderByTimestampDesc(sessionId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No se encontraron puntos de tracking para la sesión"));

        return new TrackingPointNotificationDTO(
                lastPoint.getSessionId(),
                lastPoint.getLatitude(),
                lastPoint.getLongitude(),
                lastPoint.getTimestamp()
        );
    }
}