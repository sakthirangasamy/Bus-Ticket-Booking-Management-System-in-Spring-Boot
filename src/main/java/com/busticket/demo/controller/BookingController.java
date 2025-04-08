package com.busticket.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.busticket.demo.Model.Booking;
import com.busticket.demo.Model.BookingStatus;
import com.busticket.demo.Model.Bus;
import com.busticket.demo.Model.User;
import com.busticket.demo.service.BookingService;
import com.busticket.demo.service.BusService;
import com.busticket.demo.service.ResourceNotFoundException;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final BusService busService;

    @Autowired
    public BookingController(BookingService bookingService, BusService busService) {
        this.bookingService = bookingService;
        this.busService = busService;
    }

    @PostMapping("/create")
    public String createBooking(
            @RequestParam Long busId,
            @RequestParam String passengerName,
            @RequestParam String passengerEmail,
            @RequestParam String passengerPhone,
            @RequestParam int numberOfSeats,
            @AuthenticationPrincipal User loggedInUser,  // Assuming you get the logged-in user here
            RedirectAttributes redirectAttributes) {
        
        // Get the bus
        Bus bus = busService.getBusById(busId)
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found"));
        
        // Create booking
        Booking booking = new Booking();
        booking.setBus(bus);
        booking.setPassengerName(passengerName);
        booking.setPassengerEmail(passengerEmail);
        booking.setPassengerPhone(passengerPhone);
        booking.setNumberOfSeats(numberOfSeats);
        booking.setBookingDate(LocalDate.now());
        booking.setStatus(BookingStatus.PENDING); // Set status to WAITING
        booking.setUser(loggedInUser);  // Set the logged-in user here
        
        bookingService.createBooking(booking);
        
        redirectAttributes.addFlashAttribute("success", "Booking created successfully! Status: WAITING");
        return "redirect:/bookings/confirmation";
    }

   
    @GetMapping("/confirmation")
    public String showConfirmation() {
        return "booking-confirmation";
    }
    
    @GetMapping
    public String getUserBookings(@AuthenticationPrincipal User user, Model model) {
        List<Booking> userBookings = bookingService.getBookingsByUser(user);
        model.addAttribute("bookings", userBookings);
        return "bookings-list"; // This will be your Thymeleaf template
    }
}