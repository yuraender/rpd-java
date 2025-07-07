package com.example.demo.repository;

import com.example.demo.entity.Auditorium;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuditoriumRepository extends JpaRepository<Auditorium, Integer> {

    List<Auditorium> findAllByDisabledFalse();

    List<Auditorium> findAllByAuditoriumNumberAndDisabledFalse(String auditoriumNumber);

    Optional<Auditorium> findByIdAndDisabledFalse(Integer id);
}
