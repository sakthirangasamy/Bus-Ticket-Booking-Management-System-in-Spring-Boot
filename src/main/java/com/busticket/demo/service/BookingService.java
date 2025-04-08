package com.busticket.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.busticket.demo.Model.Booking;
import com.busticket.demo.Model.BookingStatus;
import com.busticket.demo.Model.User;
import com.busticket.demo.repository.BookingRepository;

@Service
public class BookingService {


    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking createBooking(Booking booking) {
        // You might want to add validation here
        // For example, check if seats are available
        
        // Set default status if not set
        if (booking.getStatus() == null) {
            booking.setStatus(BookingStatus.PENDING);
        }
        
        return bookingRepository.save(booking);
    }

    public List<Booking> getPendingBookings() {
        return bookingRepository.findByStatus(BookingStatus.PENDING);
    }

    @Transactional
    public boolean approveBooking(Long id) {
        return updateBookingStatus(id, BookingStatus.CONFIRMED);
    }

    @Transactional
    public boolean rejectBooking(Long id) {
        return updateBookingStatus(id, BookingStatus.REJECTED);
    }

    public boolean updateBookingStatus(Long id, BookingStatus status) {
        Optional<Booking> optionalBooking = bookingRepository.findById(id);
        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();
            booking.setStatus(status);
            bookingRepository.save(booking);
            return true;
        }
        return false;
    }

    // Additional useful methods
    public Optional<Booking> findBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByStatus(status);
    }
    public List<Booking> getBookingsByUser(User user) {
        return bookingRepository.findByUserOrderByBookingDateDesc(user);
    }
    public long getTodaysBookingsCount() {
        LocalDate today = LocalDate.now(); // Get today's date
        return bookingRepository.countByBookingDate(today); // Assumes 'countByBookingDate' is a custom query in your repository
    }
    

}