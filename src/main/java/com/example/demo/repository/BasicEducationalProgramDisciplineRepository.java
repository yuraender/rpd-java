package com.example.demo.repository;

import com.example.demo.entity.BasicEducationalProgram;
import com.example.demo.entity.BasicEducationalProgramDiscipline;
import com.example.demo.entity.Discipline;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BasicEducationalProgramDisciplineRepository extends JpaRepository<BasicEducationalProgramDiscipline, Integer> {

    @EntityGraph(attributePaths = {"basicEducationalProgram", "discipline", "indicators", "auditoriums", "protocols"})
    List<BasicEducationalProgramDiscipline> findAllByDisabledFalse();

    @EntityGraph(attributePaths = {"basicEducationalProgram", "discipline", "indicators", "auditoriums", "protocols"})
    List<BasicEducationalProgramDiscipline> findAllByIndexAndBasicEducationalProgram(
            String index, BasicEducationalProgram bep
    );

    @EntityGraph(attributePaths = {"basicEducationalProgram", "discipline", "indicators", "auditoriums", "protocols"})
    List<BasicEducationalProgramDiscipline> findAllByBasicEducationalProgramAndDiscipline(
            BasicEducationalProgram bep, Discipline discipline
    );

    Optional<BasicEducationalProgramDiscipline> findByIdAndDisabledFalse(Integer id);
}
