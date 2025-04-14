package com.example.demo.repository;

import com.example.demo.entity.Indicator;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IndicatorRepository extends JpaRepository<Indicator, Integer> {

    @EntityGraph(attributePaths = {"competence"})
    List<Indicator> findAllByDisabledFalse();

    @EntityGraph(attributePaths = {"competence"})
    List<Indicator> findAllByTextAndDisabledFalse(String text);

    Optional<Indicator> findByIdAndDisabledFalse(Integer id);
}
