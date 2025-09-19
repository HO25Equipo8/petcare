package com.petcare.back.domain.dto.response;

import com.petcare.back.domain.enumerated.IncidentsTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IncidentsResponseDTO {
    private String description;
    private IncidentsTypes incidentsType;
    private Instant incidentsDate;

    // IDs de los involucrados
    private Long ownerId;
    private Long sitterId;
    private Long petId;
}
