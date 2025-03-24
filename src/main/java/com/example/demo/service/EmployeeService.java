package com.example.demo.service;

import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public List<Employee> getAll() {
        return employeeRepository.findAllByDisabledFalse()
                .stream()
                .filter(e -> !e.isDisabled())
                .toList();
    }

    public Employee getById(Integer id) {
        return employeeRepository.findByIdAndDisabledFalse(id)
                .filter(e -> !e.isDisabled())
                .orElse(null);
    }

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }
}
