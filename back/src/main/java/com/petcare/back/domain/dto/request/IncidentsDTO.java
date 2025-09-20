package com.petcare.back.domain.dto.request;

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
public class IncidentsDTO {

    private IncidentsTypes incidentsType;
    private String description;

    private Instant incidentsDate;


    private Long bookingId;


}




