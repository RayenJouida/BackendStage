package com.example.backendstage.repository;

import com.example.backendstage.entity.Role;
import com.example.backendstage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    User save(User user);

    Boolean existsByEmail(String email);
    Optional<User> findByCin(String cin);
    Boolean existsByCin(String cin);
    Boolean existsByPrenom(String prenom);
    Boolean existsByNom(String nom);
    long countByRole(Role role);

    List<User> findByActive(boolean active); // New method to find users by active status
}
