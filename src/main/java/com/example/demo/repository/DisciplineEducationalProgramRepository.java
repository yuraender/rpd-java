package com.example.demo.repository;

import com.example.demo.entity.DisciplineEducationalProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DisciplineEducationalProgramRepository extends JpaRepository<DisciplineEducationalProgram, Long> {

    Optional<DisciplineEducationalProgram> findByIdAndDisabledFalse(Long id);

    List<DisciplineEducationalProgram> findAllByDisabledFalse();
}
