package com.example.frontend.model;

import com.google.gson.annotations.SerializedName;

public class Projet {
    @SerializedName("id")
    private int id;

    @SerializedName("etudiant_id")
    private int etudiantId;

    @SerializedName("sujet")
    private String sujet;

    @SerializedName("description")
    private String description;

    @SerializedName("encadrant_id")
    private Integer encadrantId;

    @SerializedName("entreprise")
    private String entreprise;

    @SerializedName("date_debut")
    private String dateDebut;

    @SerializedName("date_fin")
    private String dateFin;

    // Constructeur
    public Projet() {}

    // Getters
    public int getId() { return id; }
    public int getEtudiantId() { return etudiantId; }
    public String getSujet() { return sujet; }
    public String getDescription() { return description; }
    public Integer getEncadrantId() { return encadrantId; }
    public String getEntreprise() { return entreprise; }
    public String getDateDebut() { return dateDebut; }
    public String getDateFin() { return dateFin; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setEtudiantId(int etudiantId) { this.etudiantId = etudiantId; }
    public void setSujet(String sujet) { this.sujet = sujet; }
    public void setDescription(String description) { this.description = description; }
    public void setEncadrantId(Integer encadrantId) { this.encadrantId = encadrantId; }
    public void setEntreprise(String entreprise) { this.entreprise = entreprise; }
    public void setDateDebut(String dateDebut) { this.dateDebut = dateDebut; }
    public void setDateFin(String dateFin) { this.dateFin = dateFin; }
}