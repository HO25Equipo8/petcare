package com.petcare.back.service;

import com.petcare.back.domain.dto.request.IncidentsDTO;
import com.petcare.back.domain.entity.IncidentImage;
import com.petcare.back.domain.entity.Incidents;
import com.petcare.back.domain.mapper.ImageValidator;
import com.petcare.back.repository.IncidentsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@Service
public class IncidentsServiceimpl implements IncidentsService {

    private final IncidentsRepository incidentsRepository;
    private final ImageValidator imageValidator;


    @Override
    public void createIncident(IncidentsDTO incidentsDTO, List<MultipartFile> images) throws IOException {
        Incidents incidents = new Incidents();
        incidents.setIncidentsType(incidentsDTO.getIncidentsType());
        incidents.setDescription(incidentsDTO.getDescription());
        incidents.setIncidentsDate(incidentsDTO.getIncidentsDate());

        if (images != null) {
            if (images.size() > 3) {
                throw new IllegalArgumentException("Only three images are allowed");
            }

            for (MultipartFile file : images) {
                imageValidator.validate(file);

                IncidentImage img = new IncidentImage();
                img.setImageName(file.getOriginalFilename());
                img.setImageType(file.getContentType());
                img.setData(file.getBytes());
                img.setIncidents(incidents);

                incidents.getImages().add(img);
            }

        }
        incidentsRepository.save(incidents);
    }

    public IncidentImage getIncidentImage(Long incidentId, Long imageId) {
        Incidents incident = incidentsRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident not found"));

        return incident.getImages().stream()
                .filter(img -> img.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Image not found"));
    }


    @Override
    public IncidentsDTO getIncidentsDTO(Long incidentId) {
        return incidentsRepository.getIncidentsById(incidentId)
                .map(incidents -> new IncidentsDTO(
                        incidents.getIncidentsType(),
                        incidents.getDescription(),
                        incidents.getIncidentsDate(),
                        incidents.getImages().stream()
                                .map(IncidentImage::getId)
                                .findFirst()
                                .orElse(null)
                )).orElseThrow(() -> new RuntimeException("Incident not found"));
    }
}