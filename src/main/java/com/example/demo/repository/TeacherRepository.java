package com.example.demo.repository;

import com.example.demo.entity.Teacher;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    @EntityGraph(attributePaths = {"employee", "department", "employeePosition", "academicDegree", "academicRank"})
    List<Teacher> findAllByDisabledFalse();

    Optional<Teacher> findByIdAndDisabledFalse(Integer id);
}
