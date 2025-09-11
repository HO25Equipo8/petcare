package com.petcare.back.domain.entity;

import com.petcare.back.domain.enumerated.IncidentResolvedStatus;
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

    @Column(name = "Incidents_date", nullable = false)
    private Instant incidentsDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @OneToMany(mappedBy = "incident", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private IncidentResolvedStatus incidentResolvedStatus;


}

