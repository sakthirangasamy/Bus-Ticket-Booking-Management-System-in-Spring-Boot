package com.busticket.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busticket.demo.Model.User;
import com.busticket.demo.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public User registerUser(User user) {
        // Simple save operation without checks
        return userRepository.save(user);
    }
    
    // Basic CRUD operations
    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }
    
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
    
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
    public long getTotalUsers() {
        return userRepository.count();
    }
}