package com.example.demo.service;

import com.example.demo.entity.Competence;
import com.example.demo.repository.CompetenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompetenceService {

    private final CompetenceRepository competenceRepository;

    public List<Competence> getAll() {
        return competenceRepository.findAllByDisabledFalse()
                .stream()
                .filter(c -> !c.isDisabled())
                .toList();
    }

    public Competence getById(Integer id) {
        return competenceRepository.findByIdAndDisabledFalse(id)
                .filter(c -> !c.isDisabled())
                .orElse(null);
    }

    public Competence save(Competence competence) {
        return competenceRepository.save(competence);
    }
}
