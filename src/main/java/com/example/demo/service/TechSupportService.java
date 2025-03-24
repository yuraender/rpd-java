package com.example.demo.service;

import com.example.demo.entity.TechSupport;
import com.example.demo.repository.TechSupportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TechSupportService {

    private final TechSupportRepository techSupportRepository;

    public List<TechSupport> getAll() {
        return techSupportRepository.findAllByDisabledFalse()
                .stream()
                .filter(ts -> !ts.isDisabled())
                .toList();
    }

    public TechSupport getById(Integer id) {
        return techSupportRepository.findByIdAndDisabledFalse(id)
                .filter(ts -> !ts.isDisabled())
                .orElse(null);
    }

    public TechSupport getByDisciplineIdAndAudienceId(Integer disciplineId, Integer audienceId) {
        return techSupportRepository.findByDisciplineIdAndAudienceIdAndDisabledFalse(disciplineId, audienceId)
                .filter(ts -> !ts.isDisabled())
                .orElse(null);
    }

    public TechSupport save(TechSupport techSupport) {
        return techSupportRepository.save(techSupport);
    }
}
