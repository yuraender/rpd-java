package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "files_rpd")
@Getter
@Setter
public class FileRPD {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private Integer academicYear;

    @Lob
    @Column(name = "section_0", columnDefinition = "LONGBLOB")
    private byte[] section0;

    @Column(name = "section_0_is_load", nullable = false)
    private boolean section0IsLoad;

    @Lob
    @Column(name = "section_1", columnDefinition = "LONGBLOB")
    private byte[] section1;

    @Column(name = "section_1_is_load", nullable = false)
    private boolean section1IsLoad;

    @Lob
    @Column(name = "section_2", columnDefinition = "LONGBLOB")
    private byte[] section2;

    @Column(name = "section_2_is_load", nullable = false)
    private boolean section2IsLoad;

    @Lob
    @Column(name = "section_3", columnDefinition = "LONGBLOB")
    private byte[] section3;

    @Column(name = "section_3_is_load", nullable = false)
    private boolean section3IsLoad;

    @Lob
    @Column(name = "section_4", columnDefinition = "LONGBLOB")
    private byte[] section4;

    @Column(name = "section_4_is_load", nullable = false)
    private boolean section4IsLoad;

    @Lob
    @Column(name = "section_5", columnDefinition = "LONGBLOB")
    private byte[] section5;

    @Column(name = "section_5_is_load", nullable = false)
    private boolean section5IsLoad;

    @Lob
    @Column(name = "section_6", columnDefinition = "LONGBLOB")
    private byte[] section6;

    @Column(name = "section_6_is_load", nullable = false)
    private boolean section6IsLoad;

    @Lob
    @Column(name = "section_7", columnDefinition = "LONGBLOB")
    private byte[] section7;

    @Column(name = "section_7_is_load", nullable = false)
    private boolean section7IsLoad;

    @Lob
    @Column(name = "section_8", columnDefinition = "LONGBLOB")
    private byte[] section8;

    @Column(name = "section_8_is_load", nullable = false)
    private boolean section8IsLoad;

    @Lob
    @Column(name = "section_9", columnDefinition = "LONGBLOB")
    private byte[] section9;

    @Column(name = "section_9_is_load", nullable = false)
    private boolean section9IsLoad;

    @ManyToOne
    @JoinColumn(name = "discipline_educational_program_id", referencedColumnName = "id", nullable = false)
    private BasicEducationalProgramDiscipline basicEducationalProgramDiscipline;

    @ManyToMany
    @JoinTable(name = "file_rpd_developers",
            joinColumns = @JoinColumn(name = "file_rpd_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "developer_id", referencedColumnName = "id"))
    private List<Teacher> developers;

    @Column(nullable = false)
    private boolean disabled;

    public List<Teacher> getDevelopers() {
        return developers.stream().filter(d -> !d.isDisabled()).toList();
    }

    public boolean isDisabled() {
        return disabled
                || basicEducationalProgramDiscipline.isDisabled()
                || developers.stream().anyMatch(Teacher::isDisabled);
    }
}
