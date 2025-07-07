package com.example.demo.repository;

import com.example.demo.entity.AcademicRank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AcademicRankRepository extends JpaRepository<AcademicRank, Integer> {

    List<AcademicRank> findAllByDisabledFalse();

    List<AcademicRank> findAllByNameAndDisabledFalse(String name);

    Optional<AcademicRank> findByIdAndDisabledFalse(Integer id);
}
