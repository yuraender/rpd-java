package com.example.demo.service;

import com.example.demo.entity.Department;
import com.example.demo.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> getAll() {
        return departmentRepository.findAll().stream()
                .filter(department -> !department.getDisabled())
                .collect(Collectors.toList());
    }

    public Department getById(Long id) {
        return departmentRepository.findById(id).orElse(null);
    }

    public Optional<Department> findByCode(String code) {
        return departmentRepository.findByCodeAndDisabledFalse(code);
    }

    public Department save(Department department) {
        return departmentRepository.save(department);
    }

    public List<Department> getAll(Long instituteId) {
        return departmentRepository.findAllByDisabledFalseAndInstituteId(instituteId);
    }

    public Department getById(Long id, Long instituteId) {
        return departmentRepository.findByIdAndDisabledFalseAndInstituteId(id, instituteId).orElse(null);
    }
}
