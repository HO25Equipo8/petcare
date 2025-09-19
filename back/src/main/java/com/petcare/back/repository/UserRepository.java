package com.petcare.back.repository;

import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByEmail(String userEmail);

    List<User> findByRole(Role role);

    List<User> findByRoleAndActive(Role role, Boolean active);

    @Query(value = """
    SELECT u.* FROM users u
    LEFT JOIN feedbacks f ON f.target_id = u.id
    WHERE u.role = 'SITTER'
    GROUP BY u.id
    ORDER BY AVG(f.rating) DESC, COUNT(f.id) DESC
    """, nativeQuery = true)
    List<User> findTopSittersByReputationNative(Pageable pageable);

    //Formula para sacar radio usa el número(6371) que es el radio de la Tierra en kilómetros.
    @Query("""
    SELECT u FROM User u
    WHERE u.role = 'SITTER'
      AND u.active = true
      AND u.checked = true
      AND u.location IS NOT NULL
      AND (6371 * acos(
            cos(radians(:lat)) *
            cos(radians(u.location.latitude)) *
            cos(radians(u.location.longitude) - radians(:lng)) +
            sin(radians(:lat)) *
            sin(radians(u.location.latitude))
      )) <= :radius
""")
    List<User> findCheckedActiveSittersWithinRadius(@Param("lat") double lat,
                                                     @Param("lng") double lng,
                                                     @Param("radius") double radiusKm);

    boolean existsByIdAndRole(Long sitterId, Role role);
}
