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
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PetTypeEnum type;

    @Column(name = "breed")
    private String breed;

    @Column(name = "age")
    private Integer age;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "color")
    private String color;

    @Enumerated(EnumType.STRING)
    @Column(name = "pet_size")
    private PetSizeEnum petSize;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "microchip")
    private String microchip;

    @Enumerated(EnumType.STRING)
    @Column(name = "temperament")
    private TemperamentEnum temperament;

    @Enumerated(EnumType.STRING)
    @Column(name = "vaccination")
    private VaccinationEnum vaccination;

    @Enumerated(EnumType.STRING)
    @Column(name = "health")
    private HealthStatusEnum health;

    @Column(name = "allergies")
    private String allergies;

    @Column(name = "medications")
    private String medications;

    @Column(name = "special_needs")
    private String specialNeeds;

    @Column(name = "emergency_contact")
    private String emergencyContact;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image imagePet;

    @OneToMany(mappedBy = "pet")
    @JsonManagedReference
    private List<Booking> bookings;
    

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User owner;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
