package com.example.frontend.model;

public class Professeur {
    private Long id;
    private String codeProf;
    private String nom; // optionnel

    public Professeur() {}

    public Professeur(Long id, String codeProf, String nom) {
        this.id = id;
        this.codeProf = codeProf;
        this.nom = nom;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodeProf() { return codeProf; }
    public void setCodeProf(String codeProf) { this.codeProf = codeProf; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
}
