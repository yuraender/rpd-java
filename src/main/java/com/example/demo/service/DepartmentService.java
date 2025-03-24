package com.example.demo.service;

import com.example.demo.entity.Department;
import com.example.demo.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public List<Department> getAll() {
        return departmentRepository.findAllByDisabledFalse()
                .stream()
                .filter(d -> !d.isDisabled())
                .toList();
    }

    public Department getById(Integer id) {
        return departmentRepository.findByIdAndDisabledFalse(id)
                .filter(d -> !d.isDisabled())
                .orElse(null);
    }

    public Department getByCode(String code) {
        return departmentRepository.findByCodeAndDisabledFalse(code)
                .filter(d -> !d.isDisabled())
                .orElse(null);
    }

    public Department save(Department department) {
        return departmentRepository.save(department);
    }
}
