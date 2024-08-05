package com.example.backendstage.service;

import com.example.backendstage.config.JwtService;
import com.example.backendstage.controller.AuthenticationRequest;
import com.example.backendstage.controller.AuthenticationResponse;
import com.example.backendstage.controller.RegisterRequest;
import com.example.backendstage.entity.Role;
import com.example.backendstage.entity.User;
import com.example.backendstage.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.management.relation.RoleNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final EmailService emailService;  // Inject email service for sending passwords

    private final Path rootLocation = Paths.get("uploads");
    private final UserServiceImp userService; // Ensure this is correctly injected


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role userRole = user.getRole();

        var jwtToken = jwtService.generateToken(user, userRole.toString());

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .role(userRole)
                .build();
    }


    public AuthenticationResponse register(RegisterRequest signUpRequest) throws RoleNotFoundException {
        validateSignUpRequest(signUpRequest);

        if (!isValidRole(signUpRequest.getRole().toString())) {
            throw new RoleNotFoundException("Invalid role");
        }

        User user = new User();
        user.setNom(signUpRequest.getNom());
        user.setPrenom(signUpRequest.getPrenom());
        user.setEmail(signUpRequest.getEmail());
        user.setRole(signUpRequest.getRole());
        user.setPhoto(signUpRequest.getPhoto());
        user.setCin(signUpRequest.getCin());
        user.setActive(true); // Set the user as active by default

        // Generate a random password
        String rawPassword = UUID.randomUUID().toString().substring(0, 8);
        user = userService.registerUserWithoutPassword(user, rawPassword);

        // Send email with the password
        String subject = "Your Account Password";
        String text = String.format("Hello %s %s,\n\nYour account has been created. Your password is: %s\n\nBest Regards,\nYour Company",
                user.getPrenom(), user.getNom(), rawPassword);
        emailService.sendSimpleMessage(user.getEmail(), subject, text);

        var jwtToken = jwtService.generateToken(user, user.getRole().toString());

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .role(user.getRole())
                .build();
    }
    public String registerUser(RegisterRequest registerRequest) throws RoleNotFoundException {
        // Create a new user from the register request
        User user = new User();
        user.setCin(registerRequest.getCin());
        user.setNom(registerRequest.getNom());
        user.setPrenom(registerRequest.getPrenom());
        user.setEmail(registerRequest.getEmail());
        user.setRole(registerRequest.getRole());
        user.setPhoto(registerRequest.getPhoto());
        user.setActive(true);  // Set user as active by default

        // Auto-generate a password
        String generatedPassword = generateRandomPassword();
        user.setPassword(generatedPassword);

        // Save the user to the repository
        userRepository.save(user);

        // Send the generated password to the user's email
        emailService.sendEmail(user.getEmail(), "Your New Account", "Your password is: " + generatedPassword);

        // Generate and return a JWT token for the new user
        return jwtService.generateToken(user, user.getRole().toString());
    }

    private String generateRandomPassword() {
        // Implement a method to generate a random password
        return UUID.randomUUID().toString();  // Example, use a more secure method in production
    }



    private boolean isValidRole(String role) {
        return "ADMIN".equals(role) || "STUDENT".equals(role) || "TEACHER".equals(role) || "SUPERVISOR".equals(role);
    }

    private void validateSignUpRequest(RegisterRequest signUpRequest) {
        if (repository.existsByCin(signUpRequest.getCin())) {
            throw new IllegalArgumentException("Error: CIN is already taken!");
        }
        if (repository.existsByEmail(signUpRequest.getEmail())) {
            throw new IllegalArgumentException("Error: Email is already in use!");
        }
    }
    public String saveFile(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
            return file.getOriginalFilename();
        } catch (Exception e) {
            throw new RuntimeException("Could not store file. Error: " + e.getMessage());
        }
    }

    public void handleToken(String token) {
        if (token != null) {
            try {
                Claims claims = jwtService.extractAllClaims(token);
                // Log or inspect claims if needed
                System.out.println("Token Claims: " + claims.toString());
            } catch (MalformedJwtException e) {
                System.out.println("Invalid JWT token: " + e.getMessage());
            }
        } else {
            System.out.println("Token is null.");
        }
    }
}
