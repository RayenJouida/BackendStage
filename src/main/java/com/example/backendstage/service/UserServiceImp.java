package com.example.backendstage.service;

import com.example.backendstage.controller.RegisterRequest;
import com.example.backendstage.entity.Role;
import com.example.backendstage.entity.User;
import com.example.backendstage.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements IUserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder encoder;  // Add this if not already present


    @Autowired
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private Environment environment;


    public User registerUser(RegisterRequest registerRequest) throws RoleNotFoundException {
        Role role = registerRequest.getRole();
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }

        User user = new User();
        user.setCin(registerRequest.getCin());
        user.setNom(registerRequest.getNom());
        user.setPrenom(registerRequest.getPrenom());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        user.setRole(role);
        user.setPhoto(registerRequest.getPhoto());
        user.setActive(true);  // Activate the user by default

        return userRepository.save(user);
    }


    @Override
    public List<User> getUser() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public User update(User user) {
        User user1 = userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("No user with id " + user.getId() + " was found!"));
        if (user1 != null) {
            userRepository.save(user);
        }
        return user1;
    }

    @Override
    public User updateById(User user) {
        User user1 = userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("No user with id " + user.getId() + " was found!"));
        if (user1 != null) {
            userRepository.save(user);
        }
        return user1;
    }

    @Override
    public void delete(Integer id) {
        userRepository.deleteById(id);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public User getUserById(Integer id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public Map<String, Integer> calculateRolePercentages() {
        long totalUsers = userRepository.count() - 1;
        if (totalUsers == 0) {
            throw new IllegalStateException("No users found in the database.");
        }

        long totalStudents = userRepository.countByRole(Role.STUDENT);
        long totalTeachers = userRepository.countByRole(Role.TEACHER);
        long totalSupervisors = userRepository.countByRole(Role.SUPERVISOR);

        int studentPercentage = (int) (((double) totalStudents / totalUsers) * 100);
        int teacherPercentage = (int) (((double) totalTeachers / totalUsers) * 100);
        int supervisorPercentage = (int) (((double) totalSupervisors / totalUsers) * 100);

        Map<String, Integer> percentages = new HashMap<>();
        percentages.put("STUDENT", studentPercentage);
        percentages.put("TEACHER", teacherPercentage);
        percentages.put("SUPERVISOR", supervisorPercentage);

        return percentages;
    }

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUserStatus(Integer id, boolean active) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No user with id " + id + " was found!"));
        user.setActive(active);
        return userRepository.save(user);
    }

    // New method for registration without a password
    public User registerUserWithoutPassword(User user, String rawPassword) {
        user.setPassword(encoder.encode(rawPassword));  // Set and encode the password
        return repository.save(user);
    }
}
