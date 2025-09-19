package com.petcare.back.repository;

import com.petcare.back.domain.entity.Offering;
import com.petcare.back.domain.enumerated.OfferingEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferingRepository extends JpaRepository<Offering, Long> {

    List<Offering> findByName(OfferingEnum name);

    List<Offering> findBySitterId(Long id);

    List<Offering> findBySitterIdAndActiveTrue(Long sitterId);
}
