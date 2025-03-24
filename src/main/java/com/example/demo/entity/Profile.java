package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "profiles")
@Getter
@Setter
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 100, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "direction_id", referencedColumnName = "id", nullable = false)
    private Direction direction;

    @Column(nullable = false)
    private boolean disabled;

    public boolean isDisabled() {
        return disabled || direction.isDisabled();
    }
}
