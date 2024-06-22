package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "disciplines")
public class Discipline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String indexDiscipline;
    private String name;
    @ManyToOne
    @JoinColumn(name = "developer_rp_id", referencedColumnName = "id")
    private Teacher developer;

    @ManyToOne
    @JoinColumn(name = "departament_id", referencedColumnName = "id")
    private Department department;

    private Boolean disabled;
    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIndexDiscipline() {
        return indexDiscipline;
    }

    public void setIndexDiscipline(String indexDiscipline) {
        this.indexDiscipline = indexDiscipline;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Teacher getDeveloper() {
        return developer;
    }

    public void setDeveloper(Teacher developer) {
        this.developer = developer;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
