package com.example.demo.repository;

import com.example.demo.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    Optional<Department> findByIdAndDisabledFalse(Integer id);

    List<Department> findAllByDisabledFalse();

    Optional<Department> findByCodeAndDisabledFalse(String code);
}