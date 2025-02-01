package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "institutes")
@Getter
@Setter
public class Institute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String city;

    @Column(columnDefinition = "TEXT")
    private String approvalText;

    @Column(columnDefinition = "TEXT")
    private String footerText;

    @ManyToOne
    @JoinColumn(name = "director_id", referencedColumnName = "id", nullable = false)
    private Employee director;

    @Column(nullable = false)
    private boolean disabled;
}
