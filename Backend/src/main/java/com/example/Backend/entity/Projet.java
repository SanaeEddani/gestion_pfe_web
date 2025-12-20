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

    private String sujet;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String entreprise;

    private LocalDate date_debut;
    private LocalDate date_fin;

    @Column(updatable = false)
    private LocalDateTime created_at;

    @Column(name = "etudiant_id", nullable = false)
    private Integer etudiantId;

    @Column(name = "encadrant_id")
    private Integer encadrantId;

    @PrePersist
    protected void onCreate() {
        this.created_at = LocalDateTime.now();
    }

    public Integer getId() { return id; }
    public String getSujet() { return sujet; }
    public void setSujet(String sujet) { this.sujet = sujet; }

    public String getEntreprise() { return entreprise; }
    public LocalDate getDate_debut() { return date_debut; }
    public LocalDate getDate_fin() { return date_fin; }

    public Integer getEtudiantId() { return etudiantId; }
    public Integer getEncadrantId() { return encadrantId; }
    public void setEncadrantId(Integer encadrantId) { this.encadrantId = encadrantId; }
}
