package com.example.demo.repository;

import com.example.demo.entity.EducationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EducationTypeRepository extends JpaRepository<EducationType, Integer> {

    List<EducationType> findAllByDisabledFalse();

    List<EducationType> findAllByNameAndDisabledFalse(String name);

    Optional<EducationType> findByIdAndDisabledFalse(Integer id);
}
