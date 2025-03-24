package com.example.demo.service;

import com.example.demo.entity.EmployeePosition;
import com.example.demo.repository.EmployeePositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeePositionService {

    private final EmployeePositionRepository employeePositionRepository;

    public List<EmployeePosition> getAll() {
        return employeePositionRepository.findAllByDisabledFalse();
    }

    public EmployeePosition getById(Integer id) {
        return employeePositionRepository.findByIdAndDisabledFalse(id).orElse(null);
    }

    public EmployeePosition save(EmployeePosition employeePosition) {
        return employeePositionRepository.save(employeePosition);
    }
}
