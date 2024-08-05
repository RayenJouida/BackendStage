package com.example.backendstage.repository;

import com.example.backendstage.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
}
