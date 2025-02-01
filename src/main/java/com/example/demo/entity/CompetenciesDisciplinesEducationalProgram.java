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
    private DisciplineEducationalProgram disciplineEducationalProgram;
    @ManyToOne
    @JoinColumn(name = "competence_id", referencedColumnName = "id")
    private Competencie competencie;
    private Boolean disabled;

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DisciplineEducationalProgram getDisciplineEducationalProgram() {
        return disciplineEducationalProgram;
    }

    public void setDisciplineEducationalProgram(DisciplineEducationalProgram disciplineEducationalProgram) {
        this.disciplineEducationalProgram = disciplineEducationalProgram;
    }

    public Competencie getCompetencie() {
        return competencie;
    }

    public void setCompetencie(Competencie competencie) {
        this.competencie = competencie;
    }
}
