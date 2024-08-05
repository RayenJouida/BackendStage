package com.example.backendstage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORSConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") // Allow requests from all origins
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Ensure all necessary HTTP methods are allowed
                .allowedHeaders("Authorization", "Content-Type", "X-Requested-With") // Specify allowed headers
                .allowCredentials(true); // Allow credentials (cookies, authorization headers, etc.)
    }
}
