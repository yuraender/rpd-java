package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "audiences")
public class Audience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "institute_id", referencedColumnName = "id")
    private Institute institute;
    private String numberAudience;
    private String tech;

    private String softwareLicense;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Institute getInstitute() {
        return institute;
    }

    public void setInstitute(Institute institute) {
        this.institute = institute;
    }

    public String getNumberAudience() {
        return numberAudience;
    }

    public void setNumberAudience(String numberAudience) {
        this.numberAudience = numberAudience;
    }

    public String getTech() {
        return tech;
    }

    public void setTech(String tech) {
        this.tech = tech;
    }

    public String getSoftwareLicense() {
        return softwareLicense;
    }

    public void setSoftwareLicense(String softwareLicense) {
        this.softwareLicense = softwareLicense;
    }
}
