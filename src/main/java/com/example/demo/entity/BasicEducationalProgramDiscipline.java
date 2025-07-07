package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "basic_educational_program_disciplines")
@Getter
@Setter
public class BasicEducationalProgramDiscipline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "discipline_index", length = 10, nullable = false)
    private String index;

    @ManyToOne
    @JoinColumn(name = "basic_educational_program_id", referencedColumnName = "id", nullable = false)
    private BasicEducationalProgram basicEducationalProgram;

    @ManyToOne
    @JoinColumn(name = "discipline_id", referencedColumnName = "id", nullable = false)
    private Discipline discipline;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "basic_educational_program_discipline_indicators",
            joinColumns = @JoinColumn(name = "basic_educational_program_discipline_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "indicator_id", referencedColumnName = "id"))
    private Set<Indicator> indicators;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "basic_educational_program_discipline_auditoriums",
            joinColumns = @JoinColumn(name = "basic_educational_program_discipline_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "auditorium_id", referencedColumnName = "id"))
    private Set<Auditorium> auditoriums;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "basic_educational_program_discipline_protocols",
            joinColumns = @JoinColumn(name = "basic_educational_program_discipline_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "protocol_id", referencedColumnName = "id"))
    private Set<Protocol> protocols;

    @Column(nullable = false)
    private boolean disabled;

    public List<Indicator> getIndicators() {
        return indicators.stream().filter(i -> !i.isDisabled()).toList();
    }

    public List<Auditorium> getAuditoriums() {
        return auditoriums.stream().filter(a -> !a.isDisabled()).toList();
    }

    public List<Protocol> getProtocols() {
        return protocols.stream().filter(p -> !p.isDisabled()).toList();
    }

    public boolean isDisabled() {
        return disabled || basicEducationalProgram.isDisabled() || discipline.isDisabled();
    }
}
