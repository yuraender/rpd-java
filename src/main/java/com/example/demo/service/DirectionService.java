package com.example.demo.service;

import com.example.demo.entity.Direction;
import com.example.demo.repository.DirectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectionService {

    private final DirectionRepository directionRepository;

    public List<Direction> getAll() {
        return directionRepository.findAllByDisabledFalse()
                .stream()
                .filter(d -> !d.isDisabled())
                .toList();
    }

    public Direction getById(Integer id) {
        return directionRepository.findByIdAndDisabledFalse(id)
                .filter(d -> !d.isDisabled())
                .orElse(null);
    }

    public boolean existsByCodeOrName(Integer id, String code, String name) {
        return directionRepository.findAllByCodeOrName(code, name)
                .stream()
                .filter(d -> !d.isDisabled())
                .anyMatch(d -> id == null || d.getId() != id);
    }

    public Direction save(Direction direction) {
        return directionRepository.save(direction);
    }
}
