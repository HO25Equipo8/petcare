package com.petcare.back.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageName;
    private String imageType;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] data;


    @ManyToOne
    @JoinColumn(name = "incident_id")
    private IncidentsTable incident;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

}