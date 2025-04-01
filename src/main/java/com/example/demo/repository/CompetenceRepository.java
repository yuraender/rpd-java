package com.example.demo.repository;

import com.example.demo.entity.Competence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompetenceRepository extends JpaRepository<Competence, Integer> {

    List<Competence> findAllByDisabledFalse();

    Optional<Competence> findByIdAndDisabledFalse(Integer id);
}
