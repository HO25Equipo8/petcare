package com.petcare.back.repository;

import com.petcare.back.domain.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query(value = "SELECT COUNT(*) FROM images WHERE user_id = :userId", nativeQuery = true)
    int countByUserId(@Param("userId") Long userId);
}
