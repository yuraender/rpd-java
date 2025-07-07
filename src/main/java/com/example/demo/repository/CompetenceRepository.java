package com.example.demo.repository;

import com.example.demo.entity.BasicEducationalProgram;
import com.example.demo.entity.Competence;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompetenceRepository extends JpaRepository<Competence, Integer> {

    @EntityGraph(attributePaths = {"basicEducationalProgram"})
    List<Competence> findAllByDisabledFalse();

    @EntityGraph(attributePaths = {"basicEducationalProgram"})
    List<Competence> findAllByIndexAndBasicEducationalProgramAndDisabledFalse(String index, BasicEducationalProgram bep);

    Optional<Competence> findByIdAndDisabledFalse(Integer id);
}
