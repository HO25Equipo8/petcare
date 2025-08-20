package com.petcare.back.domain.entity;

import com.petcare.back.domain.enumerated.ComboEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "combo_services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComboService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComboEnum name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double discount; // %

    @ManyToMany
    @JoinTable(
            name = "combo_service_services",
            joinColumns = @JoinColumn(name = "combo_service_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<Service> services;
}
