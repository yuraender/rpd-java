package com.example.demo.repository;

import com.example.demo.entity.CompetenciesDisciplinesEducationalProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompetenciesDisciplinesEducationalProgramRepository extends JpaRepository<CompetenciesDisciplinesEducationalProgram, Integer> {

    Optional<CompetenciesDisciplinesEducationalProgram> findByIdAndDisabledFalse(Integer id);

    List<CompetenciesDisciplinesEducationalProgram> findAllByDisabledFalse();
}
