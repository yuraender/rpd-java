package com.example.demo.repository;

import com.example.demo.entity.BasicEducationalProgram;
import com.example.demo.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BasicEducationalProgramRepository extends JpaRepository<BasicEducationalProgram, Long> {
    Optional<BasicEducationalProgram> findByIdAndDisabledFalse(Long id);
    List<BasicEducationalProgram> findAllByDisabledFalse();
}
