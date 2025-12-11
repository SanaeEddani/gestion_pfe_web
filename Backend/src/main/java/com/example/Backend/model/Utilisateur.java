package com.example.Backend.model;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "utilisateurs")
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    private String email;
    private String passwordHash;
    private String filiere;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    // Optionnel pour appogee et code_prof
    @Column(name="appogee_id")
    private Integer appogeeId;

    @Column(name="code_prof_id")
    private Integer codeProfId;

    private String departement;

    private LocalDateTime createdAt;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
