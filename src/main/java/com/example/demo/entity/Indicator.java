package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "indicators")
@Getter
@Setter
public class Indicator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "TEXT", nullable = false, unique = true)
    private String text;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne
    @JoinColumn(name = "competencie_id", referencedColumnName = "id", nullable = false)
    private Competencie competencie;

    @Column(nullable = false)
    private boolean disabled;

    public boolean isDisabled() {
        return disabled || competencie.isDisabled();
    }

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    @Getter
    @RequiredArgsConstructor
    public enum Type {

        KNOW("Знать"),
        BE_ABLE("Уметь"),
        OWN("Владеть");

        private final int id = ordinal();
        private final String name;
    }
}
