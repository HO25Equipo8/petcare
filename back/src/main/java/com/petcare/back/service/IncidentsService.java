package com.petcare.back.service;


import com.petcare.back.domain.dto.request.IncidentsDTO;
import com.petcare.back.domain.entity.IncidentImage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IncidentsService {

    void createIncident(IncidentsDTO incidentsDTO, List<MultipartFile> images)throws IOException;
    IncidentImage getIncidentImage(Long incidentId, Long imageId);
    IncidentsDTO getIncidentsDTO(Long incidentId);
}




