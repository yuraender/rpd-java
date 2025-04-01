package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Date;

@Entity
@Table(name = "protocols")
@Getter
@Setter
public class Protocol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "number", nullable = false)
    private int numberProtocol;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @ColumnDefault("'1970-01-01'")
    private Date date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(nullable = false)
    private boolean disabled;

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    @Getter
    @RequiredArgsConstructor
    public enum Type {

        APPROVE("Утверждено"),
        ACTUALIZE("Актуализировано");

        private final int id = ordinal();
        private final String name;
    }
}
