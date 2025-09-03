package com.petcare.back.domain.entity;

import com.petcare.back.domain.enumerated.IncidentsTypes;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "incident", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> imageIds = new ArrayList<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @ManyToOne(optional = false) //
    @JoinColumn(name = "sitter_id")
    private User sitter;

    @Column(name = "Incidents_date", nullable = false)
    private Instant incidentsDate = Instant.now();
}