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

    public List<EmployeePosition> getAllByType(EmployeePosition.Type type) {
        return employeePositionRepository.findAllByTypeAndDisabledFalse(type);
    }

    public EmployeePosition getById(Integer id) {
        return employeePositionRepository.findByIdAndDisabledFalse(id).orElse(null);
    }

    public EmployeePosition getByIdAndType(Integer id, EmployeePosition.Type type) {
        return employeePositionRepository.findByIdAndTypeAndDisabledFalse(id, type).orElse(null);
    }

    public boolean existsByName(Integer id, String name) {
        return employeePositionRepository.findAllByNameAndDisabledFalse(name)
                .stream()
                .anyMatch(ep -> id == null || ep.getId() != id);
    }

    public EmployeePosition save(EmployeePosition employeePosition) {
        return employeePositionRepository.save(employeePosition);
    }
}
