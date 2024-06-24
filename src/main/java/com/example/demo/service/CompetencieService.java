package com.example.demo.service;

import com.example.demo.entity.Competencie;
import com.example.demo.entity.EducationType;
import com.example.demo.repository.CompetencieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CompetencieService {
    private final CompetencieRepository competencieRepository;

    public CompetencieService(CompetencieRepository competencieRepository) {
        this.competencieRepository = competencieRepository;
    }

    public List<Competencie> getAll() {
        return competencieRepository.findAllByDisabledFalse();
    }

    public Competencie getById(Long id) {
        return competencieRepository.findById(id).orElse(null);
    }
    public Competencie save(Competencie entity) {
        return competencieRepository.save(entity);
    }
}
