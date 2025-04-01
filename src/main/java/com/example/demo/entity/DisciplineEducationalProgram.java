package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "disciplines_educational_programs")
@Getter
@Setter
public class DisciplineEducationalProgram {

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

    @ManyToMany
    @JoinTable(name = "basic_educational_program_discipline_indicators",
            joinColumns = @JoinColumn(name = "basic_educational_program_discipline_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "indicator_id", referencedColumnName = "id"))
    private List<Indicator> indicators;

    @ManyToMany
    @JoinTable(name = "basic_educational_program_discipline_audiences",
            joinColumns = @JoinColumn(name = "basic_educational_program_discipline_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "audience_id", referencedColumnName = "id"))
    private List<Audience> audiences;

    @ManyToMany
    @JoinTable(name = "basic_educational_program_discipline_protocols",
            joinColumns = @JoinColumn(name = "basic_educational_program_discipline_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "protocol_id", referencedColumnName = "id"))
    private List<Protocol> protocols;

    @Column(nullable = false)
    private boolean disabled;

    public List<Indicator> getIndicators() {
        return indicators.stream().filter(i -> !i.isDisabled()).toList();
    }

    public List<Audience> getAudiences() {
        return audiences.stream().filter(a -> !a.isDisabled()).toList();
    }

    public List<Protocol> getProtocols() {
        return protocols.stream().filter(p -> !p.isDisabled()).toList();
    }

    public boolean isDisabled() {
        return disabled
                || basicEducationalProgram.isDisabled() || discipline.isDisabled()
                || indicators.stream().anyMatch(Indicator::isDisabled)
                || audiences.stream().anyMatch(Audience::isDisabled)
                || protocols.stream().anyMatch(Protocol::isDisabled);
    }
}
