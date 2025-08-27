package com.petcare.back.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Entity
@Setter
public class IncidentImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageName;

    private String imageType;

    @Lob
    private byte[] data;

    @ManyToOne
    @JoinColumn(name = "incident_id")
    private Incidents incidents;


}