package com.busticket.demo.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.busticket.demo.Model.Booking;
import com.busticket.demo.Model.BookingStatus;
import com.busticket.demo.Model.User;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    // Basic query methods
    List<Booking> findByStatus(BookingStatus status);
    List<Booking> findByUserOrderByBookingDateDesc(User user);
    
    // Custom query with JOIN FETCH to avoid N+1 problem
    @Query("SELECT b FROM Booking b JOIN FETCH b.bus WHERE b.user = :user ORDER BY b.bookingDate DESC")
    List<Booking> findBookingsByUserWithBus(@Param("user") User user);
    
    // Additional useful methods
    List<Booking> findByBusId(Long busId);
    Optional<Booking> findByIdAndUser(Long id, User user);
    long countByBookingDate(LocalDate date);
   
    
    // Add this if you need to fetch with relationships
    @EntityGraph(attributePaths = {"user", "bus", "bus.route"})
    List<Booking> findTop5ByOrderByBookingDateDesc();
    
}