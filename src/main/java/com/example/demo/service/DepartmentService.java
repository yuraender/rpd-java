package com.example.demo.service;

import com.example.demo.entity.Department;
import com.example.demo.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public List<Department> getAll() {
        return departmentRepository.findAllByDisabledFalse()
                .stream()
                .filter(d -> !d.isDisabled())
                .sorted(Comparator.comparing(Department::getId))
                .toList();
    }

    public Department getById(Integer id) {
        return departmentRepository.findByIdAndDisabledFalse(id)
                .filter(d -> !d.isDisabled())
                .orElse(null);
    }

    public boolean existsByCodeOrName(Integer id, String code, String name) {
        return departmentRepository.findAllByCodeOrName(code, name)
                .stream()
                .filter(d -> !d.isDisabled())
                .anyMatch(d -> id == null || d.getId() != id);
    }

    public Department save(Department department) {
        return departmentRepository.save(department);
    }
}
