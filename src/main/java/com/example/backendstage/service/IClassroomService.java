package com.example.backendstage.service;

import com.example.backendstage.entity.Classroom;
import com.example.backendstage.entity.User;

import java.util.List;
import java.util.Optional;

public interface IClassroomService {
    List<Classroom> getAllClassrooms();
    Optional<Classroom> findById(Long id);
    Classroom addClassroom(Classroom classroom);
    Classroom updateClassroom(Classroom classroom);
    void deleteClassroom(Long id);
    void addUserToClassroom(Long classroomId, User user);
    void removeUserFromClassroom(Long classroomId, User user);
}
