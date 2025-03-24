package com.example.demo.repository;

import com.example.demo.entity.Competencie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompetencieRepository extends JpaRepository<Competencie, Integer> {

    List<Competencie> findAllByDisabledFalse();

    Optional<Competencie> findByIdAndDisabledFalse(Integer id);
}
