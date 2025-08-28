package com.petcare.back.service;

import com.petcare.back.domain.dto.request.IncidentsDTO;
import com.petcare.back.domain.entity.Image;
import com.petcare.back.domain.entity.Incidents;
import com.petcare.back.infra.error.ImageValidator;
import com.petcare.back.repository.ImageRepository;
import com.petcare.back.repository.IncidentsRepository;
import lombok.AllArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@AllArgsConstructor
@Service
public class IncidentServiceImpl  implements IncidentsService{

    private final IncidentsRepository incidentsRepository;
    private final ImageRepository imageRepository;
    private final ImageValidator imageValidator;

    @Value("${upload.dir:uploads/incidents}")
    private String uploadDir;

    @Override
    public Image uploadImage(MultipartFile file) throws IOException {
        // 1. Validar
        imageValidator.validate(file);

        // 2. Redimensionar / comprimir con Thumbnailator
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Thumbnails.of(file.getInputStream())
                .size(1280, 720)       // resolución máxima
                .outputQuality(0.7)    // calidad
                .toOutputStream(baos);

        // 3. Guardar en DB
        Image image = new Image();
        image.setImageName(file.getOriginalFilename());
        image.setImageType(file.getContentType());
        image.setData(baos.toByteArray());

        return imageRepository.save(image);
    }

    @Override
    public void createIncident(IncidentsDTO incidentsDTO, MultipartFile imageFile) throws IOException {
        Incidents incident = new Incidents();
        incident.setIncidentsType(incidentsDTO.getIncidentsType());
        incident.setDescription(incidentsDTO.getDescription());
        incident.setOwner(incidentsDTO.getOwner());
        incident.setPet(incidentsDTO.getPet());
        incident.setSitter(incidentsDTO.getSitter());
        incident.setIncidentsDate(incidentsDTO.getIncidentsDate());

        if (imageFile != null && !imageFile.isEmpty()) {
            Image image = uploadImage(imageFile);
            incident.setImage(image);
        }

        incidentsRepository.save(incident);
    }

    @Override
    public IncidentsDTO getIncidentsDTO(Long incidentId) {
        return incidentsRepository.findById(incidentId)
                .map(incident -> new IncidentsDTO(
                        incident.getIncidentsType(),
                        incident.getDescription(),
                        incident.getIncidentsDate(),
                        incident.getImage(),
                        incident.getOwner(),
                        incident.getPet(),
                        incident.getSitter()
                ))
                .orElseThrow(() -> new RuntimeException("Incident not found"));
    }
}