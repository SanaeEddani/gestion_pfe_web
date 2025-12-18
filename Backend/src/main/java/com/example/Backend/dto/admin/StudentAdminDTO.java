package com.example.Backend.dto.admin;

public class StudentAdminDTO {

    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String filiere;
    private String apogee;  // ← AJOUTER CE CHAMP
    private boolean affecte;
    private String encadrantNom;



    public String getApogee() { return apogee; }
    public void setApogee(String apogee) { this.apogee = apogee; }
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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFiliere() {
        return filiere;
    }

    public void setFiliere(String filiere) {
        this.filiere = filiere;
    }

    public boolean isAffecte() {
        return affecte;
    }

    public void setAffecte(boolean affecte) {
        this.affecte = affecte;
    }

    public String getEncadrantNom() {
        return encadrantNom;
    }

    public void setEncadrantNom(String encadrantNom) {
        this.encadrantNom = encadrantNom;
    }
}
