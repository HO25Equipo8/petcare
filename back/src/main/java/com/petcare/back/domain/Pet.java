package com.petcare.back.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Table(name = "pets")
@Entity(name = "Pet")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private String breed;
    private String size;
    private LocalDate birthDate;

    @ManyToOne
    @JoinColumn(name = "user_id") // foreign key in pet table
    @JsonBackReference   // matches with User.pets
    private User owner;
}
