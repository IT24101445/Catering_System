package com.example.catering_system.booking.repository;

import com.example.catering_system.booking.entity.EventBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventBookingRepository extends JpaRepository<EventBooking, Long> {
    List<EventBooking> findByEmail(String email);
    List<EventBooking> findByStatus(EventBooking.BookingStatus status);
    List<EventBooking> findByEventDateBetween(LocalDate startDate, LocalDate endDate);
}
