package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "competences")
@Getter
@Setter
public class Competence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "competence_index", length = 100, nullable = false)
    private String index;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String essence;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne
    @JoinColumn(name = "basic_educational_program_id", referencedColumnName = "id")
    private BasicEducationalProgram basicEducationalProgram;

    @Column(nullable = false)
    private boolean disabled;

    public boolean isDisabled() {
        return disabled || (basicEducationalProgram != null && basicEducationalProgram.isDisabled());
    }

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    @Getter
    @RequiredArgsConstructor
    public enum Type {

        U("Универсальная"),
        OP("Общепрофессиональная"),
        P("Профессиональная");

        private final int id = ordinal();
        private final String name;
    }
}
