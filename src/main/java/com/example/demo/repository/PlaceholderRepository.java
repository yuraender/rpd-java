package com.example.demo.repository;

import com.example.demo.entity.Placeholder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceholderRepository extends JpaRepository<Placeholder, Integer> {

}
