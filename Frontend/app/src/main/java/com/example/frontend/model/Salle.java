package com.example.frontend.model;

public class Salle {

    private Long id;
    private String nom;

    // Constructeur vide
    public Salle() {
    }

    // Constructeur avec paramètres
    public Salle(Long id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return nom; // Important pour l'affichage dans le Spinner
    }
}
