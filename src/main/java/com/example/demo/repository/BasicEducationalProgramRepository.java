package com.example.demo.repository;

import com.example.demo.entity.BasicEducationalProgram;
import com.example.demo.entity.EducationType;
import com.example.demo.entity.Profile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BasicEducationalProgramRepository extends JpaRepository<BasicEducationalProgram, Integer> {

    @EntityGraph(attributePaths = {"profile", "educationType", "protocol"})
    List<BasicEducationalProgram> findAllByDisabledFalse();

    @EntityGraph(attributePaths = {"profile", "educationType", "protocol"})
    List<BasicEducationalProgram> findAllByAcademicYearAndProfileAndEducationTypeAndDisabledFalse(
            Integer academicYear, Profile profile, EducationType educationType
    );

    Optional<BasicEducationalProgram> findByIdAndDisabledFalse(Integer id);
}
