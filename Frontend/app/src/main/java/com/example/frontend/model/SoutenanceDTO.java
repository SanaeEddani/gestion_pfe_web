package com.example.frontend.model;

import java.time.LocalDateTime;

public class SoutenanceDTO {

    private Long id;
    private String dateDebut;
    private String dateFin;
    private String salle;

    private String nomEncadrant;
    private String departement;
    private int nombreEtudiants;

    public SoutenanceDTO() {}

    public SoutenanceDTO(Long projetId, long l, LocalDateTime localDateTime, LocalDateTime localDateTime1) {

    }

    public Long getId() { return id; }
    public String getDateDebut() { return dateDebut; }
    public String getDateFin() { return dateFin; }
    public String getSalle() { return salle; }
    public String getNomEncadrant() { return nomEncadrant; }
    public String getDepartement() { return departement; }
    public int getNombreEtudiants() { return nombreEtudiants; }
}
