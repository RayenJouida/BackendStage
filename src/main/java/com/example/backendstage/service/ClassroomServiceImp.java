package com.example.backendstage.service;

import com.example.backendstage.entity.Classroom;
import com.example.backendstage.entity.User;
import com.example.backendstage.repository.ClassroomRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ClassroomServiceImp implements IClassroomService{
    @Autowired
    private ClassroomRepository classroomRepository;

    @Override
    public List<Classroom> getAllClassrooms() {
        return classroomRepository.findAll();
    }

    @Override
    public Optional<Classroom> findById(Long id) {
        return classroomRepository.findById(id);
    }

    @Override
    public Classroom addClassroom(Classroom classroom) {
        return classroomRepository.save(classroom);
    }


    @Override
    public Classroom updateClassroom(Classroom classroom) {
        Classroom existingClassroom = classroomRepository.findById(classroom.getClassroomId())
                .orElseThrow(() -> new EntityNotFoundException("No classroom with id " + classroom.getClassroomId() + " was found!"));
        existingClassroom.setCapacite(classroom.getCapacite());
        existingClassroom.setNomClasse(classroom.getNomClasse());
        existingClassroom.setUsers(classroom.getUsers()); // Assuming you want to update the list of users as well
        return classroomRepository.save(existingClassroom);
    }



    @Override
    public void deleteClassroom(Long id) {
        classroomRepository.deleteById(id);
    }



    @Override
    public void addUserToClassroom(Long classroomId, User user) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new EntityNotFoundException("No classroom with id " + classroomId + " was found!"));
        classroom.getUsers().add(user);
        classroomRepository.save(classroom);
    }



    @Override
    public void removeUserFromClassroom(Long classroomId, User user) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new EntityNotFoundException("No classroom with id " + classroomId + " was found!"));
        classroom.getUsers().remove(user);
        classroomRepository.save(classroom);
    }

}
