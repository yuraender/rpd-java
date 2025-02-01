package com.example.demo.repository;

import com.example.demo.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    List<Teacher> findByDisabledFalse();

    List<Teacher> findByDepartmentIdAndDisabledFalse(Long departmentId);

    Optional<Teacher> findByIdAndDisabledFalse(Long id);
}
