package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "protocols")
@Getter
@Setter
public class Protocol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int number1;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @ColumnDefault("'2000-01-01'")
    private Date date1;

    @Column(nullable = false)
    private int number2;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @ColumnDefault("'2000-01-01'")
    private Date date2;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "protocol_developers",
            joinColumns = @JoinColumn(name = "protocol_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "developer_id", referencedColumnName = "id"))
    private Set<Teacher> developers;

    @Column(nullable = false)
    private boolean disabled;

    public List<Teacher> getDevelopers() {
        return developers.stream().filter(d -> !d.isDisabled()).toList();
    }

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
