package com.example.backendstage.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classroomId;

    @Column(nullable = false)
    private int capacite;

    @Column(nullable = false, unique = true)
    private String nomClasse;

    @ManyToMany
    @JoinTable(
            name = "classroom_user",
            joinColumns = @JoinColumn(name = "classroom_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users;
}
