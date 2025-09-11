package com.petcare.back.repository;

import com.petcare.back.domain.entity.Booking;
import com.petcare.back.domain.entity.Feedback;
import com.petcare.back.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByTargetOrderByCreatedAtDesc(User target);
    @Query("""
    SELECT COUNT(f) > 0 FROM Feedback f
    WHERE f.author = :author
      AND f.target = :target
      AND f.booking = :booking
""")
    boolean existsByAuthorAndTargetAndBooking(
            @Param("author") User author,
            @Param("target") User target,
            @Param("booking") Booking booking
    );
    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.target.id = :userId")
    Double getAverageRatingForUser(@Param("userId") Long userId);

    @Query("SELECT COUNT(f) FROM Feedback f WHERE f.target.id = :userId")
    Integer getFeedbackCountForUser(@Param("userId") Long userId);
}
