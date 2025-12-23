package com.example.Backend.dto;

public class EtudiantProfileDTO {
    private String nom;
    private String prenom;
    private String email;
    private String filiere;
    private String departement;
    private String numAppogee;
    private String role;

    public EtudiantProfileDTO(String nom, String prenom, String email, String filiere,
                              String departement, String numAppogee, String role) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.filiere = filiere;
        this.departement = departement;
        this.numAppogee = numAppogee;
        this.role = role;
    }

    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getFiliere() { return filiere; }
    public String getDepartement() { return departement; }
    public String getNumAppogee() { return numAppogee; }
    public String getRole() { return role; }
}
