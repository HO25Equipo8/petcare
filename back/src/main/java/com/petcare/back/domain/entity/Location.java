package com.petcare.back.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "locations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Ej: "Sucursal San Lorenzo"

    private String address; // Calle y número

    private String city;

    private String province;

    private String country;

    private Double latitude;

    private Double longitude;

    @OneToMany(mappedBy = "location")
    private List<Schedule> schedules; // Horarios disponibles en esta sede

    @OneToMany(mappedBy = "location")
    private List<Service> services; // Servicios ofrecidos en esta sede

    // Getters, setters, constructor vacío y constructor con campos
}

