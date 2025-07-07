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

    @ManyToOne
    @JoinColumn(name = "employee_position_id", referencedColumnName = "id", nullable = false)
    private EmployeePosition employeePosition;

    @Column(nullable = false)
    private boolean disabled;

    public String getNameTypeOne() {
        String nameTypeOne = lastName;
        if (!firstName.isEmpty()) {
            nameTypeOne += " " + firstName.charAt(0) + ".";
        }
        if (middleName != null && !middleName.isEmpty()) {
            nameTypeOne += " " + middleName.charAt(0) + ".";
        }
        return nameTypeOne;
    }

    public String getNameTypeTwo() {
        String nameTypeTwo = "";
        if (!firstName.isEmpty()) {
            nameTypeTwo += firstName.charAt(0) + ". ";
        }
        if (middleName != null && !middleName.isEmpty()) {
            nameTypeTwo += middleName.charAt(0) + ". ";
        }
        nameTypeTwo += lastName;
        return nameTypeTwo;
    }

    public boolean isDisabled() {
        return disabled || employeePosition.isDisabled();
    }
}
