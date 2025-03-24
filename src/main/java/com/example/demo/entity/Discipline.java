package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "disciplines")
@Getter
@Setter
public class Discipline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "discipline_index", length = 10, nullable = false)
    private String index;

    @Column(length = 100, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "developer_id", referencedColumnName = "id", nullable = false)
    private Teacher developer;

    @Column(nullable = false)
    private boolean disabled;

    public boolean isDisabled() {
        return disabled || developer.isDisabled();
    }
}
