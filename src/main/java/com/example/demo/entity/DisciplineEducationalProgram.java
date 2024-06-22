package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "disciplines_educational_programs")
public class DisciplineEducationalProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "discipline_id", referencedColumnName = "id")
    private Discipline discipline;

    @ManyToOne
    @JoinColumn(name = "basic_educational_program_id", referencedColumnName = "id")
    private BasicEducationalProgram basicEducationalProgram;
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

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public BasicEducationalProgram getBasicEducationalProgram() {
        return basicEducationalProgram;
    }

    public void setBasicEducationalProgram(BasicEducationalProgram basicEducationalProgram) {
        this.basicEducationalProgram = basicEducationalProgram;
    }
}
