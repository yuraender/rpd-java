package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "files_rpd")
public class FileRPD {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "discipline_educational_program_id", referencedColumnName = "id")
    private DisciplineEducationalProgram disciplineEducationalProgram;

    @Lob
    @Column(name = "section_0", columnDefinition = "LONGBLOB")
    private byte[] section0;

    @Column(name = "section_0_is_load")
    private Boolean section0IsLoad;
    @Lob
    @Column(name = "section_1", columnDefinition = "LONGBLOB")
    private byte[] section1;

    @Column(name = "section_1_is_load")
    private Boolean section1IsLoad;

    @Lob
    @Column(name = "section_2", columnDefinition = "LONGBLOB")
    private byte[] section2;

    @Column(name = "section_2_is_load")
    private Boolean section2IsLoad;

    @Lob
    @Column(name = "section_3", columnDefinition = "LONGBLOB")
    private byte[] section3;

    public byte[] getSection0() {
        return section0;
    }

    public void setSection0IsLoad(Boolean section0IsLoad) {
        this.section0IsLoad = section0IsLoad;
    }

    @Column(name = "section_3_is_load")
    private Boolean section3IsLoad;

    @Lob
    @Column(name = "section_4", columnDefinition = "LONGBLOB")
    private byte[] section4;

    @Column(name = "section_4_is_load")
    private Boolean section4IsLoad;

    @Lob
    @Column(name = "section_5", columnDefinition = "LONGBLOB")
    private byte[] section5;

    @Column(name = "section_5_is_load")
    private Boolean section5IsLoad;

    @Lob
    @Column(name = "section_6", columnDefinition = "LONGBLOB")
    private byte[] section6;

    @Column(name = "section_6_is_load")
    private Boolean section6IsLoad;

    @Lob
    @Column(name = "section_7", columnDefinition = "LONGBLOB")
    private byte[] section7;
    @Column(name = "section_7_is_load")
    private Boolean section7IsLoad;

    @Lob
    @Column(name = "section_8", columnDefinition = "LONGBLOB")
    private byte[] section8;

    @Column(name = "section_8_is_load")
    private Boolean section8IsLoad;

    @Lob
    @Column(name = "section_9", columnDefinition = "LONGBLOB")
    private byte[] section9;

    @Column(name = "section_9_is_load")
    private Boolean section9IsLoad;
    private Boolean disabled;

    public void setSection0(byte[] section0) {
        this.section0 = section0;
    }

    public Boolean getSection0IsLoad() {
        return section0IsLoad;
    }

    public byte[] getSection7() {
        return section7;
    }

    public void setSection7(byte[] section7) {
        this.section7 = section7;
    }

    public Boolean getSection7IsLoad() {
        return section7IsLoad;
    }

    public void setSection7IsLoad(Boolean section7IsLoad) {
        this.section7IsLoad = section7IsLoad;
    }

    public byte[] getSection8() {
        return section8;
    }

    public void setSection8(byte[] section8) {
        this.section8 = section8;
    }

    public Boolean getSection8IsLoad() {
        return section8IsLoad;
    }

    public void setSection8IsLoad(Boolean section8IsLoad) {
        this.section8IsLoad = section8IsLoad;
    }

    public byte[] getSection9() {
        return section9;
    }

    public void setSection9(byte[] section) {
        this.section9 = section;
    }

    public Boolean getSection9IsLoad() {
        return section9IsLoad;
    }

    public void setSection9IsLoad(Boolean section9IsLoad) {
        this.section9IsLoad = section9IsLoad;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    // Геттеры и сеттеры
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DisciplineEducationalProgram getDisciplineEducationalProgram() {
        return disciplineEducationalProgram;
    }

    public void setDisciplineEducationalProgram(DisciplineEducationalProgram disciplineEducationalProgram) {
        this.disciplineEducationalProgram = disciplineEducationalProgram;
    }

    public byte[] getSection1() {
        return section1;
    }

    public void setSection1(byte[] section1) {
        this.section1 = section1;
    }

    public Boolean getSection1IsLoad() {
        return section1IsLoad;
    }

    public void setSection1IsLoad(Boolean section1IsLoad) {
        this.section1IsLoad = section1IsLoad;
    }

    public byte[] getSection2() {
        return section2;
    }

    public void setSection2(byte[] section2) {
        this.section2 = section2;
    }

    public Boolean getSection2IsLoad() {
        return section2IsLoad;
    }

    public void setSection2IsLoad(Boolean section2IsLoad) {
        this.section2IsLoad = section2IsLoad;
    }

    public byte[] getSection3() {
        return section3;
    }

    public void setSection3(byte[] section3) {
        this.section3 = section3;
    }

    public Boolean getSection3IsLoad() {
        return section3IsLoad;
    }

    public void setSection3IsLoad(Boolean section3IsLoad) {
        this.section3IsLoad = section3IsLoad;
    }

    public byte[] getSection4() {
        return section4;
    }

    public void setSection4(byte[] section4) {
        this.section4 = section4;
    }

    public Boolean getSection4IsLoad() {
        return section4IsLoad;
    }

    public void setSection4IsLoad(Boolean section4IsLoad) {
        this.section4IsLoad = section4IsLoad;
    }

    public byte[] getSection5() {
        return section5;
    }

    public void setSection5(byte[] section5) {
        this.section5 = section5;
    }

    public Boolean getSection5IsLoad() {
        return section5IsLoad;
    }

    public void setSection5IsLoad(Boolean section5IsLoad) {
        this.section5IsLoad = section5IsLoad;
    }

    public byte[] getSection6() {
        return section6;
    }

    public void setSection6(byte[] section6) {
        this.section6 = section6;
    }

    public Boolean getSection6IsLoad() {
        return section6IsLoad;
    }

    public void setSection6IsLoad(Boolean section6IsLoad) {
        this.section6IsLoad = section6IsLoad;
    }
}
