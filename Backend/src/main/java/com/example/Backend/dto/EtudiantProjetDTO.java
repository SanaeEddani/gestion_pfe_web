package com.example.Backend.dto;

import java.time.LocalDate;
import java.io.Serializable;

public class EtudiantProjetDTO implements Serializable {

    private Integer projetId;
    private Integer etudiantId;
    private String nom;
    private String prenom;
    private String filiere;
    private String sujet;
    private String entreprise;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    public EtudiantProjetDTO(
            Integer projetId,
            Integer etudiantId,
            String nom,
            String prenom,
            String filiere,
            String sujet,
            String entreprise,
            LocalDate dateDebut,
            LocalDate dateFin
    ) {
        this.projetId = projetId;
        this.etudiantId = etudiantId;
        this.nom = nom;
        this.prenom = prenom;
        this.filiere = filiere;
        this.sujet = sujet;
        this.entreprise = entreprise;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    // getters
    public Integer getProjetId() { return projetId; }
    public Integer getEtudiantId() { return etudiantId; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getFiliere() { return filiere; }
    public String getSujet() { return sujet; }
    public String getEntreprise() { return entreprise; }
    public LocalDate getDateDebut() { return dateDebut; }
    public LocalDate getDateFin() { return dateFin; }
}
