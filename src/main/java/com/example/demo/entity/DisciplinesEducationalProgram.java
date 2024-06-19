package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "disciplines_educational_programs")
public class DisciplinesEducationalProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "discipline_id", referencedColumnName = "id")
    private Discipline discipline;

    @ManyToOne
    @JoinColumn(name = "basic_educational_program_id", referencedColumnName = "id")
    private BasicEducationalProgram basicEducationalProgram;

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
