package com.example.demo.service;

import com.example.demo.entity.Protocol;
import com.example.demo.repository.ProtocolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProtocolService {

    private final ProtocolRepository protocolRepository;

    public List<Protocol> getAll() {
        return protocolRepository.findAllByDisabledFalse();
    }

    public List<Protocol> getAllByType(Protocol.Type type) {
        return protocolRepository.findAllByTypeAndDisabledFalse(type);
    }

    public Protocol getById(Integer id) {
        return protocolRepository.findByIdAndDisabledFalse(id).orElse(null);
    }

    public Protocol getByIdAndType(Integer id, Protocol.Type type) {
        return protocolRepository.findByIdAndTypeAndDisabledFalse(id, type).orElse(null);
    }

    public boolean existsByNumberAndDateAndType(Integer id, int numberProtocol, Date date, Protocol.Type type) {
        return protocolRepository.findAllByNumberProtocolAndDateAndTypeAndDisabledFalse(numberProtocol, date, type)
                .stream()
                .filter(p -> !p.isDisabled())
                .anyMatch(p -> id == null || p.getId() != id);
    }

    public Protocol save(Protocol protocol) {
        return protocolRepository.save(protocol);
    }
}
