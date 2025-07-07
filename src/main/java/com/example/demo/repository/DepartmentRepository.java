package com.example.demo.repository;

import com.example.demo.entity.Department;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    @EntityGraph(attributePaths = {"head"})
    List<Department> findAllByDisabledFalse();

    @EntityGraph(attributePaths = {"head"})
    List<Department> findAllByCodeOrName(String code, String name);

    Optional<Department> findByIdAndDisabledFalse(Integer id);
}
