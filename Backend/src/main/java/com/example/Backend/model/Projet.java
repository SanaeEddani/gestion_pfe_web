package com.example.Backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "projets")
public class Projet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ===================== RELATIONS ===================== */

    // Étudiant
    // ➜ On garde la relation ManyToOne (plus souple que OneToOne)
    @ManyToOne
    @JoinColumn(name = "etudiant_id")
    private Utilisateur etudiant;

    // Encadrant (optionnel selon ton besoin)
    @ManyToOne
    @JoinColumn(name = "encadrant_id")
    private Utilisateur encadrant;

    /* ===================== CHAMPS ===================== */

    private String sujet;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String entreprise;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /* ===================== LIFECYCLE ===================== */

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    /* ===================== GETTERS / SETTERS ===================== */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Utilisateur getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Utilisateur etudiant) {
        this.etudiant = etudiant;
    }

    public Utilisateur getEncadrant() {
        return encadrant;
    }

    public void setEncadrant(Utilisateur encadrant) {
        this.encadrant = encadrant;
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
