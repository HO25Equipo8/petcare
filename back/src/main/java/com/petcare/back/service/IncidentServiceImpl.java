package com.petcare.back.service;

import com.petcare.back.domain.dto.request.IncidentsDTO;
import com.petcare.back.domain.dto.response.IncidentsResponseDTO;
import com.petcare.back.domain.entity.*;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.exception.MyException;
import com.petcare.back.infra.error.ImageTreatment;
import com.petcare.back.infra.error.ImageValidator;
import com.petcare.back.repository.*;
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
    private final IncidentsTableRepository incidentsTableRepository;
    private final ImageTreatment imageTreatment;


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
    public Long createIncident(IncidentsDTO incidentsDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User sitterAuth = (User) authentication.getPrincipal();

        if (sitterAuth.getRole() != Role.SITTER) {
            throw new IllegalArgumentException("Solo las sitter pueden reportar incidentes");
        }

        // 🔹 Buscar la reserva
        Booking booking = bookingRepository.findById(incidentsDTO.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));


        // 🔹 Sacar las demás entidades desde el booking
        User owner = booking.getOwner();
        Pet pet = booking.getPet();

        // 🔹 Crear el incidente
        Incidents incident = new Incidents();
        incident.setIncidentsType(incidentsDTO.getIncidentsType());
        incident.setDescription(incidentsDTO.getDescription());
        incident.setBooking(booking);
        incident.setIncidentsDate(incidentsDTO.getIncidentsDate() != null
                ? incidentsDTO.getIncidentsDate()
                : Instant.now());


        incidentsRepository.save(incident);

        // 🔹 Guardar en tu tabla "IncidentsTable"
        IncidentsTable relation = new IncidentsTable();
        relation.setOwnerId(owner.getId());
        relation.setSitterId(sitterAuth.getId());
        relation.setPetId(pet.getId());
        relation.setIncidentId(incident.getId());
        incidentsTableRepository.save(relation);

        return incident.getId();
    }

    // 2️⃣ Agregar imágenes a incidente ya creado
    @Override
    public void addImagesToIncident(Long incidentId, List<MultipartFile> imageFiles) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User sitterAuth = (User) authentication.getPrincipal();

        if (sitterAuth.getRole() != Role.SITTER) {
            throw new IllegalArgumentException("Solo las sitter pueden reportar incidentes");
        }

        // 1️⃣ Buscar el IncidentsTable asociado al incidente
        IncidentsTable incidentsTable = incidentsTableRepository.findByIncidentId(incidentId)
                .orElseThrow(() -> new RuntimeException("IncidentsTable not found"));

        // 2️⃣ Validar límite de 3
        if (incidentsTable.getImages().size() + imageFiles.size() > 3) {
            throw new IllegalArgumentException("Un incidente no puede tener más de 3 imágenes");
        }

        // 3️⃣ Procesar imágenes
        for (MultipartFile file : imageFiles) {
            if (!file.isEmpty()) {
                imageValidator.validate(file); // Validar formato/tamaño
                byte[] optimizedImage = imageTreatment.process(file); // Comprimir

                Image image = new Image();
                image.setImageName(file.getOriginalFilename());
                image.setImageType(file.getContentType());
                image.setData(optimizedImage);
                image.setIncident(incidentsTable);

                incidentsTable.getImages().add(image);
            }
        }

        // 4️⃣ Guardar con cascada
        incidentsTableRepository.save(incidentsTable);
    }

    // 3️⃣ Obtener incidente
    @Override
    public IncidentsResponseDTO getIncidentsDTO(Long incidentId) {
        // 1️⃣ Buscar incidente
        Incidents incident = incidentsRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident not found"));

        // 2️⃣ Buscar tabla de IDs
        IncidentsTable table = incidentsTableRepository.findByIncidentId(incidentId)
                .orElseThrow(() -> new RuntimeException("IncidentsTable not found"));

        // 3️⃣ Mapear a DTO
        IncidentsResponseDTO dto = new IncidentsResponseDTO();
        dto.setDescription(incident.getDescription());
        dto.setIncidentsType(incident.getIncidentsType());
        dto.setIncidentsDate(incident.getIncidentsDate());

        dto.setOwnerId(table.getOwnerId());
        dto.setSitterId(table.getSitterId());
        dto.setPetId(table.getPetId());

        return dto;
    }
    // 4️⃣ Obtener imágenes de un incidente
    @Override
    public List<Image> getIncidentImages(Long incidentId) {
        IncidentsTable incidentsTable = incidentsTableRepository.findByIncidentId(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident not found"));
        return incidentsTable.getImages();
    }
}
