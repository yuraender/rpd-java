package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "basic_educational_programs")
@Getter
@Setter
public class BasicEducationalProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Integer academicYear;

    @ManyToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "id", nullable = false)
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "education_type_id", referencedColumnName = "id", nullable = false)
    private EducationType educationType;

    @Column(nullable = false)
    private boolean disabled;
}
