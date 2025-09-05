package com.petcare.back.repository;

import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByEmail(String userEmail);

    List<User> findByRole(Role role);

    @Query(value = """
    SELECT u.* FROM users u
    LEFT JOIN feedback f ON f.target_id = u.id
    WHERE u.role = 'SITTER'
    GROUP BY u.id
    ORDER BY AVG(f.rating) DESC, COUNT(f.id) DESC
    """, nativeQuery = true)
    List<User> findTopSittersByReputationNative(Pageable pageable);
}
