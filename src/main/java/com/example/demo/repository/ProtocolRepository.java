package com.example.demo.repository;

import com.example.demo.entity.Protocol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface ProtocolRepository extends JpaRepository<Protocol, Integer> {

    List<Protocol> findAllByDisabledFalse();

    List<Protocol> findAllByTypeAndDisabledFalse(Protocol.Type type);

    List<Protocol> findAllByNumberProtocolAndDateAndTypeAndDisabledFalse(
            int numberProtocol, Date date, Protocol.Type type
    );

    Optional<Protocol> findByIdAndDisabledFalse(Integer id);

    Optional<Protocol> findByIdAndTypeAndDisabledFalse(Integer id, Protocol.Type type);
}
