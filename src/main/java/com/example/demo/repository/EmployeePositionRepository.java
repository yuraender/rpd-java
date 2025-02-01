package com.example.demo.repository;

import com.example.demo.entity.EmployeePosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeePositionRepository extends JpaRepository<EmployeePosition, Integer> {

    List<EmployeePosition> findAllByDisabledFalse();
}
