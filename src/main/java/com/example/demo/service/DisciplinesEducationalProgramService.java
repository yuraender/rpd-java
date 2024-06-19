package com.example.demo.service;

import com.example.demo.entity.DisciplinesEducationalProgram;
import com.example.demo.repository.DisciplinesEducationalProgramRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisciplinesEducationalProgramService {
    private final DisciplinesEducationalProgramRepository disciplinesEducationalProgramRepository;

    public DisciplinesEducationalProgramService(DisciplinesEducationalProgramRepository disciplinesEducationalProgramRepository) {
        this.disciplinesEducationalProgramRepository = disciplinesEducationalProgramRepository;
    }

    public List<DisciplinesEducationalProgram> getAll() {
        return disciplinesEducationalProgramRepository.findAll();
    }

    public DisciplinesEducationalProgram getById(Long id) {
        return disciplinesEducationalProgramRepository.findById(id).orElse(null);
    }
}
