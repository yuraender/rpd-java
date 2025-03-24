package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "competencies_disciplines_educational_programs")
@Getter
@Setter
public class CompetenciesDisciplinesEducationalProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "discipline_educational_program_id", referencedColumnName = "id", nullable = false)
    private DisciplineEducationalProgram disciplineEducationalProgram;

    @ManyToOne
    @JoinColumn(name = "competencie_id", referencedColumnName = "id", nullable = false)
    private Competencie competencie;

    @Column(nullable = false)
    private boolean disabled;

    public boolean isDisabled() {
        return disabled || disciplineEducationalProgram.isDisabled() || competencie.isDisabled();
    }
}
