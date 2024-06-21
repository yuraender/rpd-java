package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tech_supports")
public class TechSupport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "discipline_id", referencedColumnName = "id")
    private Discipline discipline;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "audiences_id", referencedColumnName = "id")
    private Audience audience;

    private Boolean disable;

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public Audience getAudience() {
        return audience;
    }

    public void setAudience(Audience audience) {
        this.audience = audience;
    }

}
