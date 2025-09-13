package com.petcare.back.repository;

import com.petcare.back.domain.entity.BookingServiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingServiceItemRepository extends JpaRepository<BookingServiceItem, Long> {
}
