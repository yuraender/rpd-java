package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "competencies")
public class Competencie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String code;
    private String essence;
    private String know;
    private String beAble;
    private String own;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEssence() {
        return essence;
    }

    public void setEssence(String essence) {
        this.essence = essence;
    }

    public String getKnow() {
        return know;
    }

    public void setKnow(String know) {
        this.know = know;
    }

    public String getBeAble() {
        return beAble;
    }

    public void setBeAble(String beAble) {
        this.beAble = beAble;
    }

    public String getOwn() {
        return own;
    }

    public void setOwn(String own) {
        this.own = own;
    }


}

