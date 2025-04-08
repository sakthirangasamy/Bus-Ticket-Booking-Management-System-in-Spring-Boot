package com.busticket.demo.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.busticket.demo.Model.Bus;

public interface BusRepository extends JpaRepository<Bus, Long> {
	  List<Bus> findBySourceAndDestination(String source, String destination);
}
