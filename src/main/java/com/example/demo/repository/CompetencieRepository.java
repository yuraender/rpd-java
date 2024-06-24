package com.example.demo.repository;

import com.example.demo.entity.Competencie;
import com.example.demo.entity.EducationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompetencieRepository extends JpaRepository<Competencie, Long> {
    List<Competencie> findAllByDisabledFalse();
}
