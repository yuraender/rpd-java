package com.example.demo.repository;

import com.example.demo.entity.AcademicDegree;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AcademicDegreeRepository extends JpaRepository<AcademicDegree, Integer> {

    List<AcademicDegree> findAllByDisabledFalse();

    Optional<AcademicDegree> findByIdAndDisabledFalse(Integer id);
}
