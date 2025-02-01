package com.example.demo.repository;

import com.example.demo.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    Optional<Department> findByIdAndDisabledFalseAndInstituteId(Integer id, Integer instituteId);

    List<Department> findAllByDisabledFalseAndInstituteId(Integer instituteId);

    Optional<Department> findByCodeAndDisabledFalse(String code);
}