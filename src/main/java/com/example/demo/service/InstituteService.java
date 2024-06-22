package com.example.demo.service;
import com.example.demo.entity.Institute;
import com.example.demo.repository.InstituteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstituteService {

    @Autowired
    private InstituteRepository instituteRepository;

    public List<Institute> getAllInstitutes() {
        return instituteRepository.findByDisabledFalse();
    }

    public List<Institute> filterInstitutes(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return getAllInstitutes();
        }
        return instituteRepository.findByNameContainingOrCityContainingAndDisabledFalse(keyword, keyword);
    }

    public Institute findById(Long id) {
        return instituteRepository.findById(id)
                .filter(institute -> !institute.getDisabled())
                .orElse(null);
    }
}