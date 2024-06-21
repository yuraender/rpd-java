package com.example.demo.repository;

import com.example.demo.entity.TechSupport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TechSupportRepository extends JpaRepository<TechSupport, Long> {
}
