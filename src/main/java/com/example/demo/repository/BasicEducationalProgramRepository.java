package com.example.demo.repository;

import com.example.demo.entity.BasicEducationalProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BasicEducationalProgramRepository extends JpaRepository<BasicEducationalProgram, Integer> {

    Optional<BasicEducationalProgram> findByIdAndDisabledFalse(Integer id);

    List<BasicEducationalProgram> findAllByDisabledFalse();
}
