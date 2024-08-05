package com.example.backendstage.controller;

import com.example.backendstage.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String cin;
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private Role role;
    private String photo;
}
