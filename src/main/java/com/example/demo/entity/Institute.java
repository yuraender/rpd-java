package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "institutes")
public class Institute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "director_id", referencedColumnName = "id")
    private Employee director;

    private String city;
    private String approvalText;
    private String footerText;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Employee getDirector() {
        return director;
    }

    public void setDirector(Employee director) {
        this.director = director;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getApprovalText() {
        return approvalText;
    }

    public void setApprovalText(String approvalText) {
        this.approvalText = approvalText;
    }

    public String getFooterText() {
        return footerText;
    }

    public void setFooterText(String footerText) {
        this.footerText = footerText;
    }
}