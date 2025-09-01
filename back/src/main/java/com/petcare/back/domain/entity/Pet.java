package com.petcare.back.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.petcare.back.domain.enumerated.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "pets")
@Entity(name = "Pet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private PetTypeEnum type;

    private String breed;

    private Integer age;

    private Double weight; //Peso

    private String color; // Color del pelaje

    @Enumerated(EnumType.STRING)
    private PetSizeEnum petSize;  // Tamaño: pequeño, mediano, grande

    private LocalDate birthDate; // Fecha de nacimiento

    private String microchip; // Número de microchip

    @Enumerated(EnumType.STRING)
    private TemperamentEnum temperament;

    @Enumerated(EnumType.STRING)
    private VaccinationEnum vaccination;

    @Enumerated(EnumType.STRING)
    private HealthStatusEnum health;

    private String allergies; // Alergias conocidas

    private String medications; // Medicamentos actuales

    private String specialNeeds; // Necesidades especiales (comportamiento, cuidados)

    private String emergencyContact; // Veterinario o contacto de emergencia

    private Boolean active = true;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image imagePet;

    @OneToMany(mappedBy = "pet")
    @JsonManagedReference
    private List<Booking> bookings;
    

    @ManyToOne
    @JoinColumn(name = "user_id") // foreign key in pet table
    @JsonBackReference   // matches with User.pets
    private User owner;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
