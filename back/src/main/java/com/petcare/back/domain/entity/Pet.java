package com.petcare.back.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.petcare.back.domain.enumerated.HealthStatusEnum;
import com.petcare.back.domain.enumerated.PetTypeEnum;
import com.petcare.back.domain.enumerated.TemperamentEnum;
import com.petcare.back.domain.enumerated.VaccinationEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    @Enumerated(EnumType.STRING)
    private TemperamentEnum temperament;

    @Enumerated(EnumType.STRING)
    private VaccinationEnum vaccination;

    @Enumerated(EnumType.STRING)
    private HealthStatusEnum health;

    private Boolean active = true;

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
