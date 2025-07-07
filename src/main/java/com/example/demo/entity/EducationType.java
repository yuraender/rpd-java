package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "education_types")
@Getter
@Setter
public class EducationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(nullable = false)
    private int learningPeriod;

    @Column(columnDefinition = "TEXT")
    private String text;

    @Column(nullable = false)
    private boolean disabled;
}
