package com.example.demo.repository;

import com.example.demo.entity.Protocol;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProtocolRepository extends JpaRepository<Protocol, Integer> {

    @EntityGraph(attributePaths = {"developers"})
    List<Protocol> findAllByDisabledFalse();

    @EntityGraph(attributePaths = {"developers"})
    List<Protocol> findAllByTypeAndDisabledFalse(Protocol.Type type);

    Optional<Protocol> findByIdAndDisabledFalse(Integer id);

    Optional<Protocol> findByIdAndTypeAndDisabledFalse(Integer id, Protocol.Type type);
}
