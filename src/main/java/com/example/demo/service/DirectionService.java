package com.example.demo.service;

import com.example.demo.entity.Department;
import com.example.demo.entity.Direction;
import com.example.demo.repository.DirectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DirectionService {
    private final DirectionRepository directionRepository;

    public DirectionService(DirectionRepository directionRepository) {
        this.directionRepository = directionRepository;
    }

    public List<Direction> getAllDirections() {
        return directionRepository.findAll();
    }

    public Direction getById(Long id) {
        return directionRepository.findById(id).orElse(null);
    }

    public List<Direction> findAllByDisabledFalse() {
        return directionRepository.findAllByDisabledFalse();
    }
    public List<Direction> getByDepartment(Department department) {
        return directionRepository.findAllByDisabledFalseAndDepartment(department);
    }

    public Direction save(Direction direction) {
        return directionRepository.save(direction);
    }
}
