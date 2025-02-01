package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "competencies")
@Getter
@Setter
public class Competencie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 100, nullable = false)
    private String code;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String essence;

    @Column(columnDefinition = "TEXT")
    private String know;

    @Column(columnDefinition = "TEXT")
    private String beAble;

    @Column(columnDefinition = "TEXT")
    private String own;

    @Column(nullable = false)
    private boolean disabled;
}
