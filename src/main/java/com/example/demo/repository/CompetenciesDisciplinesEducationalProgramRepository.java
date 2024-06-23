package com.example.demo.repository;

import com.example.demo.entity.CompetenciesDisciplinesEducationalProgram;
import com.example.demo.entity.DisciplineEducationalProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompetenciesDisciplinesEducationalProgramRepository extends JpaRepository<CompetenciesDisciplinesEducationalProgram, Long> {
    Optional<CompetenciesDisciplinesEducationalProgram> findByIdAndDisabledFalse(Long id);
    List<CompetenciesDisciplinesEducationalProgram> findAllByDisabledFalse();
}
