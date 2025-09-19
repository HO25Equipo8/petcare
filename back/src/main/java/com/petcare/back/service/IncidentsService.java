package com.petcare.back.service;


import com.petcare.back.domain.dto.request.IncidentsDTO;
import com.petcare.back.domain.entity.Image;
import com.petcare.back.exception.MyException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IncidentsService {

    void addImagesToIncident(Long incidentId, List<MultipartFile> imageFiles) throws IOException;
    IncidentsDTO getIncidentsDTO(Long incidentId);
    Long createIncident(IncidentsDTO incidentsDTO)throws MyException;
    List<Image> getIncidentImages(Long incidentId);
    void ResolvedIncidents(Long incidentId);
}




