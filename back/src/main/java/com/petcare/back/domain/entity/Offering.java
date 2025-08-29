package com.petcare.back.domain.entity;

import com.petcare.back.domain.enumerated.PetTypeEnum;
import com.petcare.back.domain.enumerated.OfferingEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "offerings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Offering {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @Enumerated(EnumType.STRING)
        @Column(name = "name", length = 50, nullable = false)
        private OfferingEnum name;

        @Column(name = "description", nullable = false)
        private String description;

        @Column(name = "base_price", precision = 10, scale = 2, nullable = false)
        private BigDecimal basePrice;

        @ElementCollection(targetClass = PetTypeEnum.class)
        @Enumerated(EnumType.STRING)
        @CollectionTable(name = "offering_pet_types", joinColumns = @JoinColumn(name = "offering_id"))
        @Column(name = "pet_type", nullable = false)
        private List<PetTypeEnum> applicablePetTypes;

        @CreationTimestamp
        @Column(name = "created_at", updatable = false)
        private LocalDateTime createdAt;

        @UpdateTimestamp
        @Column(name = "updated_at")
        private LocalDateTime updatedAt;
}

