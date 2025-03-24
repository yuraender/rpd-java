package com.example.demo.service;

import com.example.demo.entity.CompetenciesDisciplinesEducationalProgram;
import com.example.demo.repository.CompetenciesDisciplinesEducationalProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompetenciesDisciplinesEducationalProgramService {

    private final CompetenciesDisciplinesEducationalProgramRepository competenciesDisciplinesEducationalProgramRepository;

    public List<CompetenciesDisciplinesEducationalProgram> getAll() {
        return competenciesDisciplinesEducationalProgramRepository.findAllByDisabledFalse()
                .stream()
                .filter(cdep -> !cdep.isDisabled())
                .toList();
    }

    public CompetenciesDisciplinesEducationalProgram getById(Integer id) {
        return competenciesDisciplinesEducationalProgramRepository.findByIdAndDisabledFalse(id)
                .filter(cdep -> !cdep.isDisabled())
                .orElse(null);
    }

    public CompetenciesDisciplinesEducationalProgram save(CompetenciesDisciplinesEducationalProgram competenciesDisciplinesEducationalProgram) {
        return competenciesDisciplinesEducationalProgramRepository.save(competenciesDisciplinesEducationalProgram);
    }
}
