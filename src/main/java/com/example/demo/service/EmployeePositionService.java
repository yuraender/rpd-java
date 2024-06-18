package com.example.demo.service;

import com.example.demo.entity.EmployeePosition;
import com.example.demo.repository.EmployeePositionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeePositionService {
    private final EmployeePositionRepository employeePositionRepository;

    public EmployeePositionService(EmployeePositionRepository employeePositionRepository) {
        this.employeePositionRepository = employeePositionRepository;
    }

    public List<EmployeePosition> getAll() {
        return employeePositionRepository.findAll();
    }

    public EmployeePosition getById(Long id) {
        return employeePositionRepository.findById(id).orElse(null);
    }
}
