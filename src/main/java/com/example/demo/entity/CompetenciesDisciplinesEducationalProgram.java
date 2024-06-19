package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "competencies_disciplines_educational_programs")
public class CompetenciesDisciplinesEducationalProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "discipline_educational_program_id", referencedColumnName = "id")
    private DisciplinesEducationalProgram disciplinesEducationalProgram;
    @ManyToOne
    @JoinColumn(name = "competence_id", referencedColumnName = "id")
    private Competencie competencie;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DisciplinesEducationalProgram getDisciplinesEducationalProgram() {
        return disciplinesEducationalProgram;
    }

    public void setDisciplinesEducationalProgram(DisciplinesEducationalProgram disciplinesEducationalProgram) {
        this.disciplinesEducationalProgram = disciplinesEducationalProgram;
    }

    public Competencie getCompetencie() {
        return competencie;
    }

    public void setCompetencie(Competencie competencie) {
        this.competencie = competencie;
    }
}
