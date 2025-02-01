package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "employees")
@Getter
@Setter
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 100, nullable = false)
    private String lastName;

    @Column(length = 100, nullable = false)
    private String firstName;

    @Column(length = 100)
    private String middleName;

    @Column(length = 10, nullable = false)
    private String nameTypeOne;

    @Column(length = 10, nullable = false)
    private String nameTypeTwo;

    @ManyToOne
    @JoinColumn(name = "employee_position_id", referencedColumnName = "id", nullable = false)
    private EmployeePosition employeePosition;

    @Column(nullable = false)
    private boolean disabled;
}
