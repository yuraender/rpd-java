package com.example.demo.repository;

import com.example.demo.entity.Direction;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DirectionRepository extends JpaRepository<Direction, Integer> {

    @EntityGraph(attributePaths = {"department"})
    List<Direction> findAllByDisabledFalse();

    Optional<Direction> findByIdAndDisabledFalse(Integer id);
}
