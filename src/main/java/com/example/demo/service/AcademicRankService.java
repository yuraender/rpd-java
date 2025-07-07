package com.example.demo.service;

import com.example.demo.entity.AcademicRank;
import com.example.demo.repository.AcademicRankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AcademicRankService {

    private final AcademicRankRepository academicRankRepository;

    public List<AcademicRank> getAll() {
        return academicRankRepository.findAllByDisabledFalse();
    }

    public AcademicRank getById(Integer id) {
        return academicRankRepository.findByIdAndDisabledFalse(id).orElse(null);
    }

    public boolean existsByName(Integer id, String name) {
        return academicRankRepository.findAllByNameAndDisabledFalse(name)
                .stream()
                .anyMatch(ar -> id == null || ar.getId() != id);
    }

    public AcademicRank save(AcademicRank academicRank) {
        return academicRankRepository.save(academicRank);
    }
}
