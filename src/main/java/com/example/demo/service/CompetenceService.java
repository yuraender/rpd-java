package com.example.demo.service;

import com.example.demo.entity.BasicEducationalProgram;
import com.example.demo.entity.Competence;
import com.example.demo.repository.CompetenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompetenceService {

    private final CompetenceRepository competenceRepository;

    public List<Competence> getAll() {
        return competenceRepository.findAllByDisabledFalse()
                .stream()
                .filter(c -> !c.isDisabled())
                .sorted(Comparator.comparing(Competence::getId))
                .toList();
    }

    public Competence getById(Integer id) {
        return competenceRepository.findByIdAndDisabledFalse(id)
                .filter(c -> !c.isDisabled())
                .orElse(null);
    }

    public boolean existsByIndexAndBasicEducationalProgram(Integer id, String index, BasicEducationalProgram bep) {
        return competenceRepository.findAllByIndexAndBasicEducationalProgramAndDisabledFalse(index, bep)
                .stream()
                .filter(c -> !c.isDisabled())
                .anyMatch(c -> id == null || c.getId() != id);
    }

    public Competence save(Competence competence) {
        return competenceRepository.save(competence);
    }
}
