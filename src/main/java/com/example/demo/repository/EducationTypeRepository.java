package com.example.demo.repository;

import com.example.demo.entity.EducationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EducationTypeRepository extends JpaRepository<EducationType, Long> {

    List<EducationType> findAllByDisabledFalse();
}
