package com.example.demo.service;

import com.example.demo.entity.AcademicDegree;
import com.example.demo.repository.AcademicDegreeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AcademicDegreeService {

    private final AcademicDegreeRepository academicDegreeRepository;

    public List<AcademicDegree> getAll() {
        return academicDegreeRepository.findAllByDisabledFalse();
    }

    public AcademicDegree getById(Integer id) {
        return academicDegreeRepository.findByIdAndDisabledFalse(id).orElse(null);
    }

    public AcademicDegree save(AcademicDegree academicDegree) {
        return academicDegreeRepository.save(academicDegree);
    }
}
