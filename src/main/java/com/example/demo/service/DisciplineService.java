package com.example.demo.service;

import com.example.demo.entity.Discipline;
import com.example.demo.repository.DisciplineRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisciplineService {

    private final DisciplineRepository disciplineRepository;

    public DisciplineService(DisciplineRepository disciplineRepository) {
        this.disciplineRepository = disciplineRepository;
    }

    public List<Discipline> getAll() {
        return disciplineRepository.findAllByDisabledFalse();
    }

    public Discipline getById(Integer id) {
        return disciplineRepository.findById(id).orElse(null);
    }

    public List<Discipline> getByTeacherId(Integer teacherId) {
        return disciplineRepository.findByDeveloperId(teacherId);
    }

    public Discipline save(Discipline employee) {
        return disciplineRepository.save(employee);
    }
}
