package com.example.demo.repository;

import com.example.demo.entity.EmployeePosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeePositionRepository extends JpaRepository<EmployeePosition, Integer> {

    List<EmployeePosition> findAllByDisabledFalse();

    List<EmployeePosition> findAllByNameAndDisabledFalse(String name);

    List<EmployeePosition> findAllByTypeAndDisabledFalse(EmployeePosition.Type type);

    Optional<EmployeePosition> findByIdAndDisabledFalse(Integer id);

    Optional<EmployeePosition> findByIdAndTypeAndDisabledFalse(Integer id, EmployeePosition.Type type);
}
