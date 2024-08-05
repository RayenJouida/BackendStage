package com.example.backendstage.service;

import com.example.backendstage.entity.User;
import com.example.backendstage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PasswordUpdateService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void updatePasswords() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            userRepository.save(user);
            // Add logging
            System.out.println("Updated password for user: " + user.getEmail());
        }
    }
}
