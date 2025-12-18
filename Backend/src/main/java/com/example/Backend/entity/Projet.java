package com.example.Backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "projets")
public class Projet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String sujet;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String entreprise;

    private LocalDate date_debut;
    private LocalDate date_fin;

    @Column(updatable = false)
    private LocalDateTime created_at;

    // 🔑 IDs seulement
    @Column(name = "etudiant_id", nullable = false)
    private Integer etudiantId;

    @Column(name = "encadrant_id")
    private Integer encadrantId;

    @PrePersist
    protected void onCreate() {
        this.created_at = LocalDateTime.now();
    }

    /* ============================
       GETTERS & SETTERS
       ============================ */

    public Integer getId() {
        return id;
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

    public LocalDate getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(LocalDate date_debut) {
        this.date_debut = date_debut;
    }

    public LocalDate getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(LocalDate date_fin) {
        this.date_fin = date_fin;
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
}
