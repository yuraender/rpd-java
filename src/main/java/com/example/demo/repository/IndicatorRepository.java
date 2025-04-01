package com.example.demo.repository;

import com.example.demo.entity.Indicator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IndicatorRepository extends JpaRepository<Indicator, Integer> {

    List<Indicator> findAllByDisabledFalse();

    Optional<Indicator> findByIdAndDisabledFalse(Integer id);
}
