package com.busticket.demo.controller;



import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.busticket.demo.Model.Bus;
import com.busticket.demo.service.BusService;

@Controller
public class BusController {

    private final BusService busService;

    @Autowired
    public BusController(BusService busService) {
        this.busService = busService;
    }

    // Display Add Bus Form
    @GetMapping("/admin/buses/add")
    public String showAddBusForm(Model model) {
        model.addAttribute("bus", new Bus());
        return "add-bus";
    }

    // Handle the form submission to add the bus
    @PostMapping("/admin/buses/add")
    public String addBus(@ModelAttribute("bus") Bus bus) {
        busService.saveBus(bus);
        return "add-bus";
    }
    @GetMapping("/admin/buses")
    public String manageBuses(Model model) {
        // Fetching all buses from the service
        List<Bus> buses = busService.getAllBuses();
        model.addAttribute("buses", buses); // Pass the list of buses to the view

        return "manageBuses";  // This will render 'manageBuses.html'
    }
    @GetMapping("/admin/buses/edit/{id}")
    public String editBus(@PathVariable("id") Long busId, Model model) {
        Bus bus = busService.getBusById(busId)
                .orElseThrow(() -> new IllegalArgumentException("Bus not found for id: " + busId));  // Throw exception if bus is not found

        model.addAttribute("bus", bus);  // Add the bus object to the model
        return "edit-bus";  // Return the Thymeleaf view (edit-bus.html)
    }

    @PostMapping("/admin/buses/edit/{id}")
    public String updateBus(@PathVariable("id") Long busId, @ModelAttribute("bus") Bus bus) {
        bus.setId(busId);  // Ensure the ID is set for updating the correct bus
        busService.saveBus(bus);  // Save the updated bus information
        return "redirect:/admin/buses";  // Redirect to the bus list page after saving
    }
    // Handle bus deletion
    @RequestMapping("/admin/buses/delete/{id}")
    public String deleteBus(@PathVariable("id") Long busId) {
        busService.deleteBus(busId);
        return "redirect:/admin/buses";  // redirect to the bus list page after deletion
    }
    @GetMapping("/search-bus")
    public String searchBuses(@RequestParam("source") String source, 
                               @RequestParam("destination") String destination, 
                               Model model) {
        // Query the database for buses matching the source and destination
        List<Bus> foundBuses = busService.searchBusesBySourceAndDestination(source, destination);

        // Add the found buses to the model
        model.addAttribute("foundBuses", foundBuses);

        // Return the view name to display results (for example, a search-results.html page)
        return "search-results";
    }
}
