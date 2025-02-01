package com.example.demo.repository;

import com.example.demo.entity.Department;
import com.example.demo.entity.Direction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DirectionRepository extends JpaRepository<Direction, Integer> {

    List<Direction> findAllByDisabledFalse();

    List<Direction> findAllByDisabledFalseAndDepartment(Department department);
}
