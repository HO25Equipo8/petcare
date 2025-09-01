package com.petcare.back.service;

import com.petcare.back.domain.dto.request.IncidentsDTO;
import com.petcare.back.domain.entity.Image;
import com.petcare.back.domain.entity.Incidents;
import com.petcare.back.domain.entity.Pet;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.exception.MyException;
import com.petcare.back.infra.error.ImageValidator;
import com.petcare.back.repository.ImageRepository;
import com.petcare.back.repository.IncidentsRepository;
import com.petcare.back.repository.PetRepository;
import com.petcare.back.repository.UserRepository;
import lombok.AllArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@AllArgsConstructor
@Service
public class IncidentServiceImpl  implements IncidentsService{

    private final IncidentsRepository incidentsRepository;
    private final ImageRepository imageRepository;
    private final ImageValidator imageValidator;
    private final PetRepository petRepository;
    private final UserRepository userRepository;

    private Image processImage(MultipartFile file) throws IOException {
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
        User user = (User) authentication.getPrincipal();

        if (user.getRole() != Role.SITTER) {
            throw new IllegalArgumentException("Solo las sitter pueden reportar incidentes");
        }

        User owner = userRepository.findById(incidentsDTO.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        User sitter = userRepository.findById(incidentsDTO.getSitterId())
                .orElseThrow(() -> new RuntimeException("Sitter not found"));
        Pet pet = petRepository.findById(incidentsDTO.getPetId())
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        Incidents incident = new Incidents();
        incident.setIncidentsType(incidentsDTO.getIncidentsType());
        incident.setDescription(incidentsDTO.getDescription());
        incident.setOwner(owner);
        incident.setPet(pet);
        incident.setSitter(sitter);
        incident.setIncidentsDate(incidentsDTO.getIncidentsDate());

        incidentsRepository.save(incident);
        return incident.getId();
    }

    // 2️⃣ Agregar imágenes a incidente ya creado
    @Override
    public void addImagesToIncident(Long incidentId, List<MultipartFile> imageFiles) throws IOException {
        Incidents incident = incidentsRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident not found"));

        if (incident.getImageIds().size() + imageFiles.size() > 3) {
            throw new IllegalArgumentException("Un incidente no puede tener más de 3 imágenes");
        }

        for (MultipartFile file : imageFiles) {
            if (!file.isEmpty()) {
                Image image = processImage(file);
                image.setIncident(incident);
                incident.getImageIds().add(image);
            }
        }

        incidentsRepository.save(incident);
    }

    // 3️⃣ Obtener incidente (sin imágenes pesadas)
    @Override
    public IncidentsDTO getIncidentsDTO(Long incidentId) {
        return incidentsRepository.findById(incidentId)
                .map(incident -> {
                    List<Long> ids = incident.getImageIds()
                            .stream()
                            .map(Image::getId)  // 🔹 solo IDs
                            .toList();

                    return new IncidentsDTO(
                            incident.getIncidentsType(),
                            incident.getDescription(),
                            incident.getIncidentsDate(),
                            ids,                           // ✅ lista de IDs
                            incident.getOwner().getId(),   // ✅ solo ID del dueño
                            incident.getPet().getId(),     // ✅ solo ID de la mascota
                            incident.getSitter().getId()   // ✅ solo ID del cuidador
                    );
                })
                .orElseThrow(() -> new RuntimeException("Incident not found"));
    }
    // 4️⃣ Obtener imágenes de un incidente
    @Override
    public List<Image> getIncidentImages(Long incidentId) {
        Incidents incident = incidentsRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident not found"));
        return incident.getImageIds();
    }
}
