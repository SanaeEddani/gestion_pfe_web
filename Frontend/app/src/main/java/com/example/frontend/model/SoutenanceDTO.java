package com.example.frontend.model;

public class SoutenanceDTO {

    private Long id;

    // 🔥 Dates envoyées au backend en ISO-8601 (String)
    private String dateDebut;
    private String dateFin;

    // Utilisés lors de la programmation
    private Long projetId;
    private Long salleId;

    // Champs utilisés lors de l’affichage (GET)
    private String salle;
    private String nomEncadrant;
    private String departement;
    private int nombreEtudiants;

    // Constructeur vide (obligatoire pour Gson)
    public SoutenanceDTO() {}

    // ✅ Constructeur pour PROGRAMMER une soutenance (POST)
    public SoutenanceDTO(Long projetId,
                         Long salleId,
                         String dateDebut,
                         String dateFin) {
        this.projetId = projetId;
        this.salleId = salleId;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    // =====================
    // GETTERS & SETTERS
    // =====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public Long getProjetId() {
        return projetId;
    }

    public void setProjetId(Long projetId) {
        this.projetId = projetId;
    }

    public Long getSalleId() {
        return salleId;
    }

    public void setSalleId(Long salleId) {
        this.salleId = salleId;
    }

    public String getSalle() {
        return salle;
    }

    public void setSalle(String salle) {
        this.salle = salle;
    }

    public String getNomEncadrant() {
        return nomEncadrant;
    }

    public void setNomEncadrant(String nomEncadrant) {
        this.nomEncadrant = nomEncadrant;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public int getNombreEtudiants() {
        return nombreEtudiants;
    }

    public void setNombreEtudiants(int nombreEtudiants) {
        this.nombreEtudiants = nombreEtudiants;
    }
}
