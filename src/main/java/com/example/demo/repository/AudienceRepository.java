package com.example.demo.repository;
import com.example.demo.entity.Audience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AudienceRepository extends JpaRepository<Audience, Long> {
    Optional<Audience> findByIdAndDisabledFalse(Long id);
    List<Audience> findAllByDisabledFalseAndInstituteId(Long instituteId);
}
