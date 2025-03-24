package com.example.demo.repository;

import com.example.demo.entity.DisciplineEducationalProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DisciplineEducationalProgramRepository extends JpaRepository<DisciplineEducationalProgram, Integer> {

    List<DisciplineEducationalProgram> findAllByDisabledFalse();

    Optional<DisciplineEducationalProgram> findByIdAndDisabledFalse(Integer id);
}
