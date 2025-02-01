package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "audiences")
@Getter
@Setter
public class Audience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 50, nullable = false)
    private String numberAudience;

    @Column(columnDefinition = "TEXT")
    private String tech;

    @Column(columnDefinition = "TEXT")
    private String softwareLicense;

    @ManyToOne
    @JoinColumn(name = "institute_id", referencedColumnName = "id", nullable = false)
    private Institute institute;

    @Column(nullable = false)
    private boolean disabled;
}
