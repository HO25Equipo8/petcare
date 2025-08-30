package com.petcare.back.repository;

import com.petcare.back.domain.entity.Offering;
import com.petcare.back.domain.enumerated.OfferingEnum;
import com.petcare.back.domain.enumerated.PetTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferingRepository extends JpaRepository<Offering, Long> {

    List<Offering> findByName(OfferingEnum name);
    Optional<Offering> findByNameAndPetTypesContaining(OfferingEnum name, PetTypeEnum petType);
}
