package com.petcare.back.service;

import com.petcare.back.domain.dto.request.IncidentsDTO;
import com.petcare.back.domain.entity.Image;
import com.petcare.back.domain.entity.Incidents;
import com.petcare.back.domain.mapper.ImageValidator;
import com.petcare.back.repository.ImageRepository;
import com.petcare.back.repository.IncidentsRepository;
import lombok.AllArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@AllArgsConstructor
@Service
public class IncidentsServiceimpl implements IncidentsService {

    private final ImageRepository imageRepository;
    private final IncidentsRepository incidentsRepository;
    private final ImageValidator imageValidator;

    // Carpeta de destino
    @Value("${upload.dir:uploads/incidents}")
    private String uploadDir;


    @Override
    public Image uploadImage(MultipartFile file) throws IOException {
        // 1. Validar formato y tamaño
        imageValidator.validate(file);

        // 2. Crear carpeta si no existe
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 3. Nombre único (para evitar choques)
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);

        // 4. Redimensionar y comprimir la imagen antes de guardarla
        try (OutputStream os = Files.newOutputStream(filePath)) {
            Thumbnails.of(file.getInputStream())
                    .size(1280, 720)        // resolución máxima
                    .outputQuality(0.7)     // calidad (0.0 a 1.0)
                    .toOutputStream(os);
        }

        // 5. Guardar referencia en DB
        Image image = new Image();
        image.setUrl("/" + uploadDir + "/" + filename);
        return imageRepository.save(image);
    }

    @Override
    public void createIncident(IncidentsDTO incidentsDTO, MultipartFile file) throws IOException {
        Incidents incidents = new Incidents();
        incidents.setIncidentsType(incidentsDTO.getIncidentsType());
        incidents.setDescription(incidentsDTO.getDescription());
        incidents.setIncidentsDate(incidentsDTO.getIncidentsDate());
        incidents.setOwner(incidentsDTO.getOwner());
        incidents.setPet(incidentsDTO.getPet());
        incidents.setSitter(incidentsDTO.getSitter());

        if (file != null) {
            imageValidator.validate(file);

            // Subir imagen al disco y guardar en BD
            String uploadDir = "uploads/incidents/";
            Files.createDirectories(Paths.get(uploadDir));

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);
            Files.copy(file.getInputStream(), filePath);

            Image img = new Image();
            img.setUrl(filePath.toString());
            incidents.setImage(img);
        }

        incidentsRepository.save(incidents);
    }


    @Override
    public IncidentsDTO getIncidentsDTO(Long incidentId) {
        return incidentsRepository.findById(incidentId)
                .map(incidents -> new IncidentsDTO(
                        incidents.getIncidentsType(),
                        incidents.getDescription(),
                        incidents.getIncidentsDate(),
                        incidents.getImage(),
                        incidents.getOwner(),
                        incidents.getPet(),
                        incidents.getSitter()
                ))
                .orElseThrow(() -> new RuntimeException("Incident not found"));
    }
}