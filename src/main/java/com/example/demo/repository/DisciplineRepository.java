package com.example.demo.repository;

import com.example.demo.entity.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DisciplineRepository extends JpaRepository<Discipline, Integer> {

    List<Discipline> findAllByDisabledFalse();

    Optional<Discipline> findByIdAndDisabledFalse(Integer id);
}
