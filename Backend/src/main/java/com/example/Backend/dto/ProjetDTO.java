package com.example.Backend.dto;

public class ProjetDTO {

    private String sujet;
    private String description;
    private String entreprise;

    public ProjetDTO() {} // constructeur vide

    public ProjetDTO(String sujet, String description, String entreprise) { // constructeur avec paramètres
        this.sujet = sujet;
        this.description = description;
        this.entreprise = entreprise;
    }

    public String getSujet() { return sujet; }
    public void setSujet(String sujet) { this.sujet = sujet; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getEntreprise() { return entreprise; }
    public void setEntreprise(String entreprise) { this.entreprise = entreprise; }
}
