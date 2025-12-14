package com.example.Backend.dto;

public class SignupRequest {
    public String nom;
    public String prenom;
    public String email;
    public String password;
    public String role;
    public String filiere;
    public String departement;
    public String appogee;
    public String codeProf;

    // Getters
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getFiliere() { return filiere; }
    public String getDepartement() { return departement; }
    public String getAppogee() { return appogee; }
    public String getCodeProf() { return codeProf; }

    // Setters
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
    public void setFiliere(String filiere) { this.filiere = filiere; }
    public void setDepartement(String departement) { this.departement = departement; }
    public void setAppogee(String appogee) { this.appogee = appogee; }
    public void setCodeProf(String codeProf) { this.codeProf = codeProf; }
}