package com.petcare.back.domain.entity;

import com.petcare.back.domain.enumerated.IncidentsTypes;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
public class Incidents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private IncidentsTypes incidentsType;

    private String description;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id") 
    private Image image;

    @OneToOne(optional = false)
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToOne(optional = false)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @OneToOne(optional = false)
    @JoinColumn(name = "sitter_id")
    private User sitter;

    @Column(name = "Incidents_date", nullable = false)
    private Instant incidentsDate = Instant.now();
}