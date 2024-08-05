package com.example.backendstage.controller;

import com.example.backendstage.entity.User;
import com.example.backendstage.repository.UserRepository;
import com.example.backendstage.service.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.backendstage.service.EmailService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController {

    @Autowired
    private UserServiceImp userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Get all users
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getUser();
        return ResponseEntity.ok(users);
    }

    // Get user by ID
    @GetMapping("/get/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        Optional<User> user = userService.findById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // Generate a random password
        String password = UUID.randomUUID().toString().substring(0, 8);
        user.setPassword(passwordEncoder.encode(password));

        User createdUser = userService.addUser(user);

        // Send email with the password
        String subject = "Your Account Password";
        String text = String.format("Hello %s %s,\n\nYour account has been created. Your password is: %s\n\nBest Regards,\nYour Company",
                user.getPrenom(), user.getNom(), password);
        emailService.sendSimpleMessage(user.getEmail(), subject, text);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // Update an existing user
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User user) {
        user.setId(id);
        User updatedUser = userService.update(user);
        return ResponseEntity.ok(updatedUser);
    }

    // Delete a user by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Integer id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Find user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(user);
    }

    // Get role percentages
    @GetMapping("/role-percentages")
    public ResponseEntity<Map<String, Integer>> getRolePercentages() {
        Map<String, Integer> percentages = userService.calculateRolePercentages();
        return ResponseEntity.ok(percentages);
    }

    @PutMapping("/update-status/{userId}")
    public ResponseEntity<?> updateUserStatus(@PathVariable Integer userId, @RequestBody Map<String, Boolean> body) {
        if (userRepository.findById(userId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Get the user and update their status
        User user = userRepository.findById(userId).get();
        user.setActive(body.get("active"));
        userRepository.save(user);
        return ResponseEntity.ok().body("User status updated");
    }

}
