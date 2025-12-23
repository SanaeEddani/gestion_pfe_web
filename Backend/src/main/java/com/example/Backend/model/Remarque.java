package com.example.Backend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="remarques")
public class Remarque {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String contenu;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name="projet_id")
    private Document projet;

    @ManyToOne
    @JoinColumn(name="encadrant_id")
    private Utilisateur encadrant;

    // getters et setters
}

