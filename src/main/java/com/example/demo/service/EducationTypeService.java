package com.example.demo.service;

import com.example.demo.entity.EducationType;
import com.example.demo.repository.EducationTypeRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EducationTypeService {
    private final EducationTypeRepository educationTypeRepository;

    public EducationTypeService(EducationTypeRepository educationTypeRepository) {
        this.educationTypeRepository = educationTypeRepository;
    }

    public List<EducationType> getAllEducationTypes() {
        return educationTypeRepository.findAll();
    }

    public EducationType getById(Long id) {
        return educationTypeRepository.findById(id).orElse(null);
    }
}
