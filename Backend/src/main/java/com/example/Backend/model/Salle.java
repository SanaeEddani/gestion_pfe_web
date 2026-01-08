package com.example.Backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "salles")
public class Salle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private int capacite;

    // getters & setters
    public Long getId() { return id; }
    public String getNom() { return nom; }
    public int getCapacite() { return capacite; }

    public void setId(Long id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setCapacite(int capacite) { this.capacite = capacite; }
}
