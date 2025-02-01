package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "education_types")
public class EducationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer learningPeriod;
    private String text;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLearningPeriod() {
        return learningPeriod;
    }

    public void setLearningPeriod(Integer learningPeriod) {
        this.learningPeriod = learningPeriod;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
