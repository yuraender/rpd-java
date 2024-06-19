package com.example.demo.service;

import com.example.demo.entity.Audience;
import com.example.demo.repository.AudienceRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AudienceService {
    private final AudienceRepository audienceRepository;

    public AudienceService(AudienceRepository audienceRepository) {
        this.audienceRepository = audienceRepository;
    }

    public List<Audience> getAll() {
        return audienceRepository.findAll();
    }

    public Audience getById(Long id) {
        return audienceRepository.findById(id).orElse(null);
    }
}
