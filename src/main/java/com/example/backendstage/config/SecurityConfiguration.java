package com.example.backendstage.config;

import com.example.backendstage.config.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/v1/auth/**", "/api/v1/files/**", "/uploads/**")  // Disable CSRF for specific endpoints
                )
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/api/v1/auth/**").permitAll()  // Permit all requests to the authentication endpoints
                        .requestMatchers("/api/v1/files/**").permitAll()  // Permit all requests to the file upload endpoints
                        .requestMatchers("/uploads/**").permitAll()  // Permit all requests to the file upload URL
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")  // Restrict access to admin endpoints
                        .requestMatchers("/api/v1/teacher/**").hasRole("TEACHER")  // Restrict access to teacher endpoints
                        .requestMatchers("/api/v1/student/**").hasRole("STUDENT")  // Restrict access to student endpoints
                        .requestMatchers("/api/v1/supervisor/**").hasRole("SUPERVISOR")  // Restrict access to supervisor endpoints
                        .anyRequest().authenticated()  // Require authentication for all other requests
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // Stateless session management
                )
                .authenticationProvider(authenticationProvider)  // Custom authentication provider
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);  // Add JWT filter before default authentication filter

        return http.build();
    }
}
