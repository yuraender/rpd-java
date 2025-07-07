package com.example.demo.service;

import com.example.demo.entity.EducationType;
import com.example.demo.repository.EducationTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EducationTypeService {

    private final EducationTypeRepository educationTypeRepository;

    public List<EducationType> getAll() {
        return educationTypeRepository.findAllByDisabledFalse();
    }

    public EducationType getById(Integer id) {
        return educationTypeRepository.findByIdAndDisabledFalse(id).orElse(null);
    }

    public boolean existsByName(Integer id, String name) {
        return educationTypeRepository.findAllByNameAndDisabledFalse(name)
                .stream()
                .anyMatch(et -> id == null || et.getId() != id);
    }

    public EducationType save(EducationType educationType) {
        return educationTypeRepository.save(educationType);
    }
}
