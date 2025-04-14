package com.example.demo.repository;

import com.example.demo.entity.BasicEducationalProgramDiscipline;
import com.example.demo.entity.FileRPD;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRPDRepository extends JpaRepository<FileRPD, Integer> {

    @EntityGraph(attributePaths = {"basicEducationalProgramDiscipline"})
    List<FileRPD> findAllByDisabledFalse();

    @EntityGraph(attributePaths = {"basicEducationalProgramDiscipline"})
    List<FileRPD> findAllByAcademicYearAndBasicEducationalProgramDisciplineAndDisabledFalse(
            Integer academicYear, BasicEducationalProgramDiscipline basicEducationalProgramDiscipline
    );

    Optional<FileRPD> findByIdAndDisabledFalse(Integer id);
}
