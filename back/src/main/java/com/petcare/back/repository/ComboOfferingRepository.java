package com.petcare.back.repository;

import com.petcare.back.domain.entity.ComboOffering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComboOfferingRepository extends JpaRepository<ComboOffering, Long> {
}
