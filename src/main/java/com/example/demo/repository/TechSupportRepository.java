package com.example.demo.repository;

import com.example.demo.entity.TechSupport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TechSupportRepository extends JpaRepository<TechSupport, Integer> {

    List<TechSupport> findAllByDisabledFalse();

    Optional<TechSupport> findByIdAndDisabledFalse(Integer id);

    Optional<TechSupport> findByDisciplineIdAndAudienceIdAndDisabledFalse(Integer disciplineIid, Integer audienceId);
}
