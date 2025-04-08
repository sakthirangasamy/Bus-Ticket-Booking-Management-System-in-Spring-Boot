package com.busticket.demo.service;



import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busticket.demo.Model.Bus;
import com.busticket.demo.repository.BusRepository;

@Service
public class BusService {

    private final BusRepository busRepository;

    @Autowired
    public BusService(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    public Bus saveBus(Bus bus) {
        return busRepository.save(bus);
    }
    public List<Bus> getAllBuses() {
        return busRepository.findAll();  // Fetching all buses from the repository
    }
    // Correct implementation - just return the Optional as-is
    public Optional<Bus> getBusById(Long busId) {
        return busRepository.findById(busId);
    }

    // Alternative implementation if you want to throw an exception
    public Bus getBusByIdOrThrow(Long busId) {
        return busRepository.findById(busId)
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found with id: " + busId));
    }
    // Delete a bus by its ID
    public void deleteBus(Long busId) {
        busRepository.deleteById(busId);  // delete the bus by its ID
    }
    public List<Bus> searchBusesBySourceAndDestination(String source, String destination) {
        // Query the database using the repository method (assuming you have this method in your repository)
        return busRepository.findBySourceAndDestination(source, destination);
    }
    public long getTotalBuses() {
        return busRepository.count();
    }
}
