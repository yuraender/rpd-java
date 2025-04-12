package com.example.demo.repository;

import com.example.demo.entity.BasicEducationalProgramDiscipline;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BasicEducationalProgramDisciplineRepository extends JpaRepository<BasicEducationalProgramDiscipline, Integer> {

    @EntityGraph(attributePaths = {"basicEducationalProgram", "discipline"})
    List<BasicEducationalProgramDiscipline> findAllByDisabledFalse();

    Optional<BasicEducationalProgramDiscipline> findByIdAndDisabledFalse(Integer id);
}
