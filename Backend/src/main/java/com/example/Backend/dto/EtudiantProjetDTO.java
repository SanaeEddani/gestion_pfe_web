package com.example.Backend.dto;

public class EtudiantProjetDTO {

    private Integer projetId;
    private Integer etudiantId;
    private String nom;
    private String prenom;
    private String filiere;
    private String sujet;

    // 🔑 CONSTRUCTEUR OBLIGATOIRE POUR JPQL
    public EtudiantProjetDTO(
            Integer projetId,
            Integer etudiantId,
            String nom,
            String prenom,
            String filiere,
            String sujet
    ) {
        this.projetId = projetId;
        this.etudiantId = etudiantId;
        this.nom = nom;
        this.prenom = prenom;
        this.filiere = filiere;
        this.sujet = sujet;
    }

    // Getters (obligatoires pour JSON)
    public Integer getProjetId() {
        return projetId;
    }

    public Integer getEtudiantId() {
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
