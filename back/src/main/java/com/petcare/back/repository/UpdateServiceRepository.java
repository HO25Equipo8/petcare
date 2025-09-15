package com.petcare.back.repository;

import com.petcare.back.domain.entity.UpdateService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface UpdateServiceRepository extends JpaRepository<UpdateService, Long> {
}
