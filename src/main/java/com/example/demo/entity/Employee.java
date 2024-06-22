package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "name_type_one")
    private String nameTypeOne;

    @Column(name = "name_type_two")
    private String nameTypeTwo;
    private Boolean disabled;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getNameTypeOne() {
        return nameTypeOne;
    }

    public void setNameTypeOne(String nameTypeOne) {
        this.nameTypeOne = nameTypeOne;
    }

    public String getNameTypeTwo() {
        return nameTypeTwo;
    }

    public void setNameTypeTwo(String nameTypeTwo) {
        this.nameTypeTwo = nameTypeTwo;
    }
}