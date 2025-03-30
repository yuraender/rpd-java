package com.example.demo.service;

import com.example.demo.entity.Teacher;
import com.example.demo.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public List<Teacher> getAll() {
        return teacherRepository.findAllByDisabledFalse()
                .stream()
                .filter(t -> !t.isDisabled())
                .toList();
    }

    public Teacher getById(Integer id) {
        return teacherRepository.findByIdAndDisabledFalse(id)
                .filter(t -> !t.isDisabled())
                .orElse(null);
    }

    public Teacher save(Teacher teacher) {
        return teacherRepository.save(teacher);
    }
}
