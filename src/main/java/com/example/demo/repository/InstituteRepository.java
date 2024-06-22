package com.example.demo.repository;

import com.example.demo.entity.Institute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstituteRepository extends JpaRepository<Institute, Long> {
    List<Institute> findByDisabledFalse();
    List<Institute> findByNameContainingOrCityContainingAndDisabledFalse(String name, String city);
    @Query("SELECT i FROM Institute i WHERE i.name LIKE %:keyword% OR i.city LIKE %:keyword%")
    List<Institute> findByNameOrCityContaining(@Param("keyword") String keyword);
}