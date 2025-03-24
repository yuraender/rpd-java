package com.example.demo.service;

import com.example.demo.entity.Audience;
import com.example.demo.repository.AudienceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AudienceService {

    private final AudienceRepository audienceRepository;

    public List<Audience> getAll() {
        return audienceRepository.findAllByDisabledFalse();
    }

    public Audience getById(Integer id) {
        return audienceRepository.findByIdAndDisabledFalse(id).orElse(null);
    }

    public Audience save(Audience audience) {
        return audienceRepository.save(audience);
    }
}
