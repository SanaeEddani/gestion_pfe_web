package com.example.Backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "soutenances")
public class Soutenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "projet_id", nullable = false)
    private Projet projet;

    @ManyToOne
    @JoinColumn(name = "salle_id", nullable = false)
    private Salle salle;

    private LocalDateTime dateHeureDebut;
    private LocalDateTime dateHeureFin;

    // getters & setters
    public Long getId() { return id; }
    public Projet getProjet() { return projet; }
    public Salle getSalle() { return salle; }
    public LocalDateTime getDateHeureDebut() { return dateHeureDebut; }
    public LocalDateTime getDateHeureFin() { return dateHeureFin; }

    public void setProjet(Projet projet) { this.projet = projet; }
    public void setSalle(Salle salle) { this.salle = salle; }
    public void setDateHeureDebut(LocalDateTime d) { this.dateHeureDebut = d; }
    public void setDateHeureFin(LocalDateTime f) { this.dateHeureFin = f; }
}
