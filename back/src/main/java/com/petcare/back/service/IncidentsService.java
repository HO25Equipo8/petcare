package com.petcare.back.service;


import com.petcare.back.domain.dto.request.IncidentsDTO;
import com.petcare.back.domain.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface IncidentsService {

    void createIncident(IncidentsDTO incidentsDTO, MultipartFile file) throws IOException;
    IncidentsDTO getIncidentsDTO(Long incidentId);
    Image uploadImage(MultipartFile file) throws IOException;
}




