package com.example.demo.repository;

import com.example.demo.entity.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DisciplineRepository extends JpaRepository<Discipline, Integer> {

    List<Discipline> findByDeveloperId(Integer developerRpId);

    List<Discipline> findAllByDisabledFalse();
}
