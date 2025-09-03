package com.petcare.back.domain.dto.request;


import com.petcare.back.domain.entity.Pet;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.IncidentsTypes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IncidentsDTO {

    private IncidentsTypes incidentsType;

    private String description;

    private Instant incidentsDate = Instant.now();

    private List<Long> imageIds;

    private Long ownerId;

    private Long petId;

    private Long sitterId;
}
