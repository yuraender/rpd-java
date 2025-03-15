package com.example.demo.service;

import com.example.demo.entity.Department;
import com.example.demo.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> getAll() {
        return departmentRepository.findAllByDisabledFalse();
    }

    public Department getById(Integer id) {
        return departmentRepository.findByIdAndDisabledFalse(id).orElse(null);
    }

    public Optional<Department> findByCode(String code) {
        return departmentRepository.findByCodeAndDisabledFalse(code);
    }

    public Department save(Department department) {
        return departmentRepository.save(department);
    }
}
