package com.petcare.back.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "locations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "province", nullable = false)
    private String province;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "place_id")
    private String placeId;
}

