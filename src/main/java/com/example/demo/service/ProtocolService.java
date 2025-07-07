package com.example.demo.service;

import com.example.demo.entity.Protocol;
import com.example.demo.repository.ProtocolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProtocolService {

    private final ProtocolRepository protocolRepository;

    public List<Protocol> getAll() {
        return protocolRepository.findAllByDisabledFalse()
                .stream()
                .sorted(Comparator.comparing(Protocol::getId))
                .toList();
    }

    public List<Protocol> getAllByType(Protocol.Type type) {
        return protocolRepository.findAllByTypeAndDisabledFalse(type)
                .stream()
                .sorted(Comparator.comparing(Protocol::getId))
                .toList();
    }

    public Protocol getById(Integer id) {
        return protocolRepository.findByIdAndDisabledFalse(id).orElse(null);
    }

    public Protocol getByIdAndType(Integer id, Protocol.Type type) {
        return protocolRepository.findByIdAndTypeAndDisabledFalse(id, type).orElse(null);
    }

    public Protocol save(Protocol protocol) {
        return protocolRepository.save(protocol);
    }
}
