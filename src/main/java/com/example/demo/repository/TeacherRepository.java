package com.example.demo.repository;

import com.example.demo.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    List<Teacher> findByDisabledFalse();

    List<Teacher> findByDepartmentIdAndDisabledFalse(Integer departmentId);

    Optional<Teacher> findByIdAndDisabledFalse(Integer id);
}
