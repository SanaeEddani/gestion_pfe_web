package com.example.frontend.model;

public class EtudiantProjetDTO {

    private int projetId;
    private int etudiantId;
    private String nom;
    private String prenom;
    private String filiere;
    private String sujet;

    public int getProjetId() {
        return projetId;
    }

    public int getEtudiantId() {
        return etudiantId;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getFiliere() {
        return filiere;
    }

    public String getSujet() {
        return sujet;
    }
}
