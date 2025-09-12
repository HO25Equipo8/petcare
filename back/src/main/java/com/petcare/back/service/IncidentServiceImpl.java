package com.petcare.back.service;

import com.petcare.back.domain.dto.request.IncidentsDTO;
import com.petcare.back.domain.dto.response.IncidentsResponseDTO;
import com.petcare.back.domain.entity.*;
import com.petcare.back.domain.enumerated.IncidentResolvedStatus;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.exception.MyException;
import com.petcare.back.infra.error.ImageTreatment;
import com.petcare.back.infra.error.ImageValidator;
import com.petcare.back.repository.*;
import com.petcare.back.validation.ValidationReportIncidents;
import lombok.AllArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;


@AllArgsConstructor
@Service
public class IncidentServiceImpl  implements IncidentsService{

    private final IncidentsRepository incidentsRepository;
    private final ImageRepository imageRepository;
    private final ImageValidator imageValidator;
    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ImageTreatment imageTreatment;
    private final List<ValidationReportIncidents> validationReportIncidents;


    public Image processImage(MultipartFile file) throws IOException {
        imageValidator.validate(file);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Thumbnails.of(file.getInputStream())
                .size(1280, 720)    // resolución máxima
                .outputQuality(0.7) // calidad comprimida
                .toOutputStream(baos);

        Image image = new Image();
        image.setImageName(file.getOriginalFilename());
        image.setImageType(file.getContentType());
        image.setData(baos.toByteArray());

        return image;
    }

    // 1️⃣ Crear incidente sin imágenes
    @Override
    public Long createIncident(IncidentsDTO incidentsDTO)  throws MyException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User sitterAuth = (User) authentication.getPrincipal();

        if (sitterAuth.getRole() != Role.SITTER) {
            throw new IllegalArgumentException("Solo las sitter pueden reportar incidentes");
        }

        // 🔹 Buscar la reserva
        Booking booking = bookingRepository.findById(incidentsDTO.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        for (ValidationReportIncidents v : validationReportIncidents) {
            v.validate(incidentsDTO);
        }

        // 🔹 Crear el incidente
        Incidents incident = new Incidents();
        incident.setIncidentsType(incidentsDTO.getIncidentsType());
        incident.setDescription(incidentsDTO.getDescription());
        incident.setIncidentResolvedStatus(IncidentResolvedStatus.NO_RESULETO);
        incident.setBooking(booking);
        incident.setIncidentsDate(
                incidentsDTO.getIncidentsDate() != null
                        ? incidentsDTO.getIncidentsDate()
                        : Instant.now()
        );

        incidentsRepository.save(incident);

        return incident.getId();
    }

    // 2️⃣ Agregar imágenes a incidente ya creado
    public void addImagesToIncident(Long incidentId, List<MultipartFile> imageFiles) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User sitterAuth = (User) authentication.getPrincipal();

        if (sitterAuth.getRole() != Role.SITTER) {
            throw new IllegalArgumentException("Solo las sitter pueden reportar incidentes");
        }

        // 1️⃣ Buscar el incidente
        Incidents incident = incidentsRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident not found"));

        // 2️⃣ Validar límite de 3
        if (incident.getImages().size() + imageFiles.size() > 3) {
            throw new IllegalArgumentException("Un incidente no puede tener más de 3 imágenes");
        }

        // 3️⃣ Procesar y agregar imágenes
        for (MultipartFile file : imageFiles) {
            if (!file.isEmpty()) {
                imageValidator.validate(file);
                byte[] optimizedImage = imageTreatment.process(file);

                Image image = new Image();
                image.setImageName(file.getOriginalFilename());
                image.setImageType(file.getContentType());
                image.setData(optimizedImage);
                image.setIncident(incident);

                incident.getImages().add(image);
            }
        }

        // 4️⃣ Guardar con cascada (Incidents guarda las imágenes)
        incidentsRepository.save(incident);
    }

    @Override
    public void ResolvedIncidents(Long incidentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User sitterAuth = (User) authentication.getPrincipal();


        if (sitterAuth.getRole() != Role.SITTER) {
            throw new IllegalArgumentException("Solo las sitter pueden resolver  incidentes");
        }

        // 1️⃣ Buscar el incidente
        Incidents incident = incidentsRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident not found"));

        incident.setIncidentResolvedStatus(IncidentResolvedStatus.RESULETO);
        incidentsRepository.save(incident);
    }

    // 3️⃣ Obtener incidente
    @Override
    public IncidentsDTO getIncidentsDTO(Long incidentId) {
        // 1️⃣ Buscar incidente
        Incidents incident = incidentsRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident not found"));


        // 3️⃣ Mapear a DTO
        IncidentsDTO dto = new IncidentsDTO();
        dto.setDescription(incident.getDescription());
        dto.setIncidentsType(incident.getIncidentsType());
        dto.setIncidentsDate(incident.getIncidentsDate());
        return dto;
    }
    // 4️⃣ Obtener imágenes de un incidente
    @Override
    public List<Image> getIncidentImages(Long incidentId) {
        Incidents incident = incidentsRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident not found"));

        return incident.getImages();
    }
}
