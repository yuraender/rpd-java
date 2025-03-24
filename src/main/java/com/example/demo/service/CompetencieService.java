package com.example.demo.service;

import com.example.demo.entity.Competencie;
import com.example.demo.repository.CompetencieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompetencieService {

    private final CompetencieRepository competencieRepository;

    public List<Competencie> getAll() {
        return competencieRepository.findAllByDisabledFalse();
    }

    public Competencie getById(Integer id) {
        return competencieRepository.findByIdAndDisabledFalse(id).orElse(null);
    }

    public Competencie save(Competencie competencie) {
        return competencieRepository.save(competencie);
    }
}
