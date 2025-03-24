package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tech_support")
@Getter
@Setter
public class TechSupport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "discipline_id", referencedColumnName = "id", nullable = false)
    private Discipline discipline;

    @ManyToOne
    @JoinColumn(name = "audience_id", referencedColumnName = "id", nullable = false)
    private Audience audience;

    @Column(nullable = false)
    private boolean disabled;

    public boolean isDisabled() {
        return disabled || discipline.isDisabled() || audience.isDisabled();
    }
}
