package com.example.demo.service;

import com.example.demo.entity.Teacher;
import com.example.demo.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public List<Teacher> getAll() {
        return teacherRepository.findByDisabledFalse();
    }

    public List<Teacher> findByDepartmentId(Integer departmentId) {
        return teacherRepository.findByDepartmentIdAndDisabledFalse(departmentId);
    }

    public Teacher getById(Integer id) {
        return teacherRepository.findByIdAndDisabledFalse(id).orElse(null);
    }

    public Teacher save(Teacher entity) {
        return teacherRepository.save(entity);
    }
}
