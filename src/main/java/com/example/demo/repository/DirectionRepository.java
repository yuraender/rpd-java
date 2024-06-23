package com.example.demo.repository;

import com.example.demo.entity.Audience;
import com.example.demo.entity.Department;
import com.example.demo.entity.Direction;
import com.example.demo.entity.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DirectionRepository extends JpaRepository<Direction, Long> {
    List<Direction> findAllByDisabledFalse();

    List<Direction> findAllByDisabledFalseAndDepartment(Department department);
}
