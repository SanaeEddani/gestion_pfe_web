package com.example.Backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "utilisateurs")
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "filiere")
    private String filiere;

    @Column(name = "departement")
    private String departement;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "appogee_id")
    private Appogee appogee;

    @ManyToOne
    @JoinColumn(name = "code_prof_id")
    private CodeProf codeProf;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getFiliere() { return filiere; }
    public void setFiliere(String filiere) { this.filiere = filiere; }

    public String getDepartement() { return departement; }
    public void setDepartement(String departement) { this.departement = departement; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Appogee getAppogee() { return appogee; }
    public void setAppogee(Appogee appogee) { this.appogee = appogee; }

    public CodeProf getCodeProf() { return codeProf; }
    public void setCodeProf(CodeProf codeProf) { this.codeProf = codeProf; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}