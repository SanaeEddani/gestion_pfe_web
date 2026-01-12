package com.example.Backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nomDocument;
    private String chemin;
    private String format;

    @Column(name="date_upload")
    private LocalDateTime dateUpload;

    @ManyToOne
    @JoinColumn(name = "etudiant_id")
    private Utilisateur etudiant;

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNomDocument() { return nomDocument; }
    public void setNomDocument(String nomDocument) { this.nomDocument = nomDocument; }

    public String getChemin() { return chemin; }
    public void setChemin(String chemin) { this.chemin = chemin; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }

    public LocalDateTime getDateUpload() { return dateUpload; }
    public void setDateUpload(LocalDateTime dateUpload) { this.dateUpload = dateUpload; }

    public Utilisateur getEtudiant() { return etudiant; }
    public void setEtudiant(Utilisateur etudiant) { this.etudiant = etudiant; }
}
