package com.petcare.back.domain.entity;

import com.petcare.back.domain.enumerated.ComboEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "combo_offerings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComboOffering {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private ComboEnum name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "discount", nullable = false)
    private Double discount; // porcentaje, ej. 10 = 10%

    @ManyToMany
    @JoinTable(
            name = "combo_offering_services",
            joinColumns = @JoinColumn(name = "combo_offering_id"),
            inverseJoinColumns = @JoinColumn(name = "offering_id")
    )
    private List<Offering> offerings;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sitter_id", nullable = false)
    private User sitter;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Transient // no se guarda en DB
    public Double getFinalPrice() {
        if (offerings == null || offerings.isEmpty()) return 0.0;
        double total = offerings.stream()
                .mapToDouble(s -> s.getBasePrice().doubleValue())
                .sum();
        return total - (total * (discount / 100));
    }
}
