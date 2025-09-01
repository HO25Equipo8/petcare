package com.petcare.back.service;


import com.petcare.back.domain.dto.request.IncidentsDTO;
import com.petcare.back.domain.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface IncidentsService {

    void addImagesToIncident(Long incidentId, List<MultipartFile> imageFiles) throws IOException;
    IncidentsDTO getIncidentsDTO(Long incidentId);
    Long createIncident(IncidentsDTO incidentsDTO);
    List<Image> getIncidentImages(Long incidentId);
}




