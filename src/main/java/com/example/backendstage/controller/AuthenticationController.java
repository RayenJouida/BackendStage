package com.example.backendstage.controller;

import org.springframework.security.core.Authentication;
import com.example.backendstage.config.JwtService;
import com.example.backendstage.entity.User;
import com.example.backendstage.service.AuthenticationService;
import com.example.backendstage.service.UserServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.management.relation.RoleNotFoundException;
import java.util.Collections;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private final UserServiceImp userService;
    @Autowired
    private final AuthenticationService service;
    @Autowired
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            String token = AuthenticationService.registerUser(registerRequest);
            return ResponseEntity.ok(token);
        } catch (RoleNotFoundException e) {
            return ResponseEntity.badRequest().body("Role not found");
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/admin/test")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> adminTest() {
        return ResponseEntity.ok("Hello Admin");
    }

    @GetMapping("/teacher/test")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> teacherTest() {
        return ResponseEntity.ok("Hello Teacher");
    }

    @GetMapping("/student/test")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> studentTest() {
        return ResponseEntity.ok("Hello Student");
    }

    @GetMapping("/supervisor/test")
    @PreAuthorize("hasRole('SUPERVISOR')")
    public ResponseEntity<?> supervisorTest() {
        return ResponseEntity.ok("Hello Supervisor");
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = service.saveFile(file);
        return ResponseEntity.ok(Collections.singletonMap("fileName", fileName));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(user);
    }
}
