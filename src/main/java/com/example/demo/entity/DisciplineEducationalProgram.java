package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "disciplines_educational_programs")
@Getter
@Setter
public class DisciplineEducationalProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "basic_educational_program_id", referencedColumnName = "id", nullable = false)
    private BasicEducationalProgram basicEducationalProgram;

    @ManyToOne
    @JoinColumn(name = "discipline_id", referencedColumnName = "id", nullable = false)
    private Discipline discipline;

    @Column(nullable = false)
    private boolean disabled;

    public boolean isDisabled() {
        return disabled || basicEducationalProgram.isDisabled() || discipline.isDisabled();
    }
}
