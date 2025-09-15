package com.petcare.back.repository;

import com.petcare.back.domain.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

}
