package com.example.Backend.dto.admin;

import java.util.List;

public class EncadrantAdminDTO {

    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String departement;
    private List<String> etudiants;

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

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public List<String> getEtudiants() {
        return etudiants;
    }

    public void setEtudiants(List<String> etudiants) {
        this.etudiants = etudiants;
    }
}
