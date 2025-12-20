package com.example.Backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "projets")
public class Projet {

    /* =====================
       PRIMARY KEY
       ===================== */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /* =====================
       FK SIMPLES (PAS DE RELATION)
       ===================== */
    @Column(name = "etudiant_id", nullable = false)
    private Integer etudiantId;

    @Column(name = "encadrant_id")
    private Integer encadrantId;

    /* =====================
       CHAMPS METIER
       ===================== */
    private String sujet;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String entreprise;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    /* =====================
       AUDIT
       ===================== */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    /* =====================
       GETTERS / SETTERS
       ===================== */
    public Integer getId() {
        return id;
    }

    public Integer getEtudiantId() {
        return etudiantId;
    }

    public void setEtudiantId(Integer etudiantId) {
        this.etudiantId = etudiantId;
    }

    public Integer getEncadrantId() {
        return encadrantId;
    }

    public void setEncadrantId(Integer encadrantId) {
        this.encadrantId = encadrantId;
    }

    public String getSujet() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(String entreprise) {
        this.entreprise = entreprise;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
