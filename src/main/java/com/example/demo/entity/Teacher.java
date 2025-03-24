package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "teachers")
@Getter
@Setter
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id", nullable = false)
    private Department department;

    @ManyToOne
    @JoinColumn(name = "employee_position_id", referencedColumnName = "id", nullable = false)
    private EmployeePosition employeePosition;

    @Column(nullable = false)
    private boolean disabled;

    public boolean isDisabled() {
        return disabled || employee.isDisabled() || department.isDisabled() || employeePosition.isDisabled();
    }
}
