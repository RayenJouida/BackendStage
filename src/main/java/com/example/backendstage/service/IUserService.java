package com.example.backendstage.service;

import com.example.backendstage.entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<User> getUser();
    Optional<User> findById(Integer id);
    User update(User user);
    User updateById(User user);
    void deleteUser(User user);
    void delete(Integer id);
    User getUserById(Integer id);
    User findByEmail(String email);
    User addUser(User user);
    User updateUserStatus(Integer id, boolean active); // New method

}
