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
        return competenciesDisciplinesEducationalProgramRepository.findAllByDisabledFalse();
    }

    public CompetenciesDisciplinesEducationalProgram getById(Integer id) {
        return competenciesDisciplinesEducationalProgramRepository.findById(id).orElse(null);
    }

    public CompetenciesDisciplinesEducationalProgram save(CompetenciesDisciplinesEducationalProgram competenciesDisciplinesEducationalProgram) {
        return competenciesDisciplinesEducationalProgramRepository.save(competenciesDisciplinesEducationalProgram);
    }
}
