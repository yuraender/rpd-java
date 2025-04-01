package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "competences")
@Getter
@Setter
public class Competence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "competence_index", length = 100, nullable = false)
    private String index;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String essence;

    @Column(nullable = false)
    private boolean disabled;
}
