package com.example.Backend.dto;

public class SalleDTO {
    private Long id;
    private String nom;

    public SalleDTO() {}

    public SalleDTO(Long id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    @Override
    public String toString() { return nom; } // Pour affichage dans Spinner
}
