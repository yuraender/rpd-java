package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "audiences")
@Getter
@Setter
public class Audience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "number", length = 50, nullable = false)
    private String audienceNumber;

    @Column(columnDefinition = "TEXT")
    private String equipment;

    @Column(columnDefinition = "TEXT")
    private String software;

    @Column(nullable = false)
    private boolean disabled;
}
