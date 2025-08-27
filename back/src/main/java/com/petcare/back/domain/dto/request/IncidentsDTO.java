package com.petcare.back.domain.dto.request;



import com.petcare.back.domain.entity.Image;
import com.petcare.back.domain.entity.Pet;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.IncidentsTypes;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IncidentsDTO {

    private IncidentsTypes incidentsType;

    private String description;

    private Instant incidentsDate = Instant.now();

    private Image imageIds;

    private User owner;


    private Pet pet;


    private User sitter;


}
