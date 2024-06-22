package com.example.demo.service;

import com.example.demo.entity.Audience;
import com.example.demo.repository.AudienceRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AudienceService {
    private final AudienceRepository audienceRepository;

    public AudienceService(AudienceRepository audienceRepository) {
        this.audienceRepository = audienceRepository;
    }

    public List<Audience> findAllByDisabledFalseAndInstituteId(Long instituteId) {
        return audienceRepository.findAllByDisabledFalseAndInstituteId(instituteId);
    }

    public List<Audience> getAll() {
        return audienceRepository.findAll().stream()
                .filter(techSupport -> !techSupport.getDisabled())
                .collect(Collectors.toList());
    }

    public Audience getById(Long id) {
        return audienceRepository.findByIdAndDisabledFalse(id).orElse(null);
    }
    public Audience save(Audience audience) {
        return audienceRepository.save(audience);
    }
}
