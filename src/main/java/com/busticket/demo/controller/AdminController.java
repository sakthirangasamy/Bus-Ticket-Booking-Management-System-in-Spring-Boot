package com.busticket.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.busticket.demo.Model.Booking;
import com.busticket.demo.Model.BookingStatus;
import com.busticket.demo.service.BookingService;
import com.busticket.demo.service.BusService;
import com.busticket.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

    private final BusService busService;
    private final BookingService bookingService;
    private final UserService userService;

    @Autowired
    public AdminController(BusService busService, BookingService bookingService, UserService userService) {
        this.busService = busService;
        this.bookingService = bookingService;
        this.userService = userService;
    }

    // Endpoint to view all bookings in the admin panel
    @GetMapping("/admin/bookings")
    public String getBookings(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "adminbooking"; // Return the Thymeleaf template for the bookings page
    }

    // Endpoint to update booking status (confirmed, cancelled, etc.)
    @PostMapping("/admin/bookings/{bookingId}/update-status")
    public String updateBookingStatus(@PathVariable Long bookingId, BookingStatus status) {
        bookingService.updateBookingStatus(bookingId, status); // Update the booking status
        return "redirect:/admin/bookings"; // Redirect back to the admin bookings page
    }

    // Display the admin login page
    @GetMapping("/admin/login")
    public String showAdminLoginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {

        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }

        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully");
        }

        return "adminlogin";
    }

    // Handle the admin login form submission
    @PostMapping("/adminlogin")
    public String handleAdminLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        // Hardcoded credentials for demonstration
        if ("admin".equals(username) && "admin".equals(password)) {
            session.setAttribute("adminLoggedIn", true);
            return "redirect:/admin/home";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "adminlogin";
        }
    }

    @GetMapping("/admin/home")
    public String showAdminHomePage(HttpSession session, Model model) {
        // Check admin authentication
        if (session.getAttribute("adminLoggedIn") == null) {
            return "redirect:/adminlogin";
        }
        
        // Get statistics
        long totalBuses = busService.getTotalBuses();
        long totalUsers = userService.getTotalUsers();
        long todaysBookings = bookingService.getTodaysBookingsCount();
        List<Booking> recentBookings = bookingService.getAllBookings();
        
        // Add to model
        model.addAttribute("totalBuses", totalBuses);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("todaysBookings", todaysBookings);
        model.addAttribute("recentBookings", recentBookings);
        
        return "adminhome";
    }
    @GetMapping("/admin/reports")
    public String showReportsPage(HttpSession session, Model model) {
    	long totalBuses = busService.getTotalBuses();
        long totalUsers = userService.getTotalUsers();
        long todaysBookings = bookingService.getTodaysBookingsCount();
        model.addAttribute("totalBuses", totalBuses);
        model.addAttribute("todaysBookings", todaysBookings);
        model.addAttribute("totalUsers", totalUsers);
        return "reports"; // This will map to reports.html
    }
    // Handle admin logout
    @GetMapping("/adminlogout")
    public String handleAdminLogout(HttpSession session) {
        session.invalidate(); // Invalidate the session when logging out
        return "index"; // Redirect to the admin login page
    }
}
