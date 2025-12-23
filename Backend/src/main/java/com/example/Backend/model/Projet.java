package com.example.Backend.model;

import jakarta.persistence.*;

@Entity
public class Projet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Long au lieu de Integer

    @ManyToOne
    @JoinColumn(name = "etudiant_id")
    private Utilisateur etudiant;

    private String sujet;
    private String description;
    private String entreprise;

    // getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Utilisateur getEtudiant() { return etudiant; }
    public void setEtudiant(Utilisateur etudiant) { this.etudiant = etudiant; }

    public String getSujet() { return sujet; }
    public void setSujet(String sujet) { this.sujet = sujet; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getEntreprise() { return entreprise; }
    public void setEntreprise(String entreprise) { this.entreprise = entreprise; }
}
