package com.example.demo.repository;

import com.example.demo.entity.Profile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {

    @EntityGraph(attributePaths = {"direction"})
    List<Profile> findAllByDisabledFalse();

    @EntityGraph(attributePaths = {"direction"})
    List<Profile> findAllByNameAndDisabledFalse(String name);

    Optional<Profile> findByIdAndDisabledFalse(Integer id);
}
