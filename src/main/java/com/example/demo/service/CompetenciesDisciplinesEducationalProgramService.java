package com.example.demo.service;

import com.example.demo.entity.CompetenciesDisciplinesEducationalProgram;
import com.example.demo.repository.CompetenciesDisciplinesEducationalProgramRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CompetenciesDisciplinesEducationalProgramService {
    private final CompetenciesDisciplinesEducationalProgramRepository competenciesDisciplinesEducationalProgramRepository;

    public CompetenciesDisciplinesEducationalProgramService(CompetenciesDisciplinesEducationalProgramRepository competenciesDisciplinesEducationalProgramRepository) {
        this.competenciesDisciplinesEducationalProgramRepository = competenciesDisciplinesEducationalProgramRepository;
    }

    public List<CompetenciesDisciplinesEducationalProgram> getAll() {
        return competenciesDisciplinesEducationalProgramRepository.findAll();
    }

    public CompetenciesDisciplinesEducationalProgram getById(Long id) {
        return competenciesDisciplinesEducationalProgramRepository.findById(id).orElse(null);
    }
}
