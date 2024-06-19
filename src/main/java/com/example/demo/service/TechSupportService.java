package com.example.demo.service;

import com.example.demo.entity.TechSupport;
import com.example.demo.repository.TechSupportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TechSupportService {
    private final TechSupportRepository techSupportRepository;

    public TechSupportService(TechSupportRepository techSupportRepository) {
        this.techSupportRepository = techSupportRepository;
    }

    public List<TechSupport> getAll() {
        return techSupportRepository.findAll();
    }

    public TechSupport getById(Long id) {
        return techSupportRepository.findById(id).orElse(null);
    }
}
