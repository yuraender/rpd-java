package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "departments")
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
    @JoinColumn(name = "head_id", referencedColumnName = "id", nullable = false)
    private Employee head;

    @Column(nullable = false)
    private boolean disabled;

    public boolean isDisabled() {
        return disabled || head.isDisabled();
    }
}
