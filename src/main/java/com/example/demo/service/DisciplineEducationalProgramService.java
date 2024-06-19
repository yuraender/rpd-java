package com.example.demo.service;

import com.example.demo.entity.DisciplineEducationalProgram;
import com.example.demo.repository.DisciplineEducationalProgramRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisciplineEducationalProgramService {
    private final DisciplineEducationalProgramRepository disciplineEducationalProgramRepository;

    public DisciplineEducationalProgramService(DisciplineEducationalProgramRepository disciplineEducationalProgramRepository) {
        this.disciplineEducationalProgramRepository = disciplineEducationalProgramRepository;
    }

    public List<DisciplineEducationalProgram> getAll() {
        return disciplineEducationalProgramRepository.findAll();
    }

    public DisciplineEducationalProgram getById(Long id) {
        return disciplineEducationalProgramRepository.findById(id).orElse(null);
    }
}
