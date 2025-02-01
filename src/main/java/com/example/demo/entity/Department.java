package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "departaments")
@Getter
@Setter
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 10, nullable = false)
    private String code;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 50)
    private String abbreviation;

    @ManyToOne
    @JoinColumn(name = "institute_id", referencedColumnName = "id", nullable = false)
    private Institute institute;

    @ManyToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "id", nullable = false)
    private Employee manager;

    @Column(nullable = false)
    private boolean disabled;
}
