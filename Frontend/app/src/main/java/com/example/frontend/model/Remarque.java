package com.example.frontend.model;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class Remarque {
    @SerializedName("id")
    private int id;

    @SerializedName("projet_id")
    private int projetId;

    @SerializedName("encadrant_id")
    private int encadrantId;

    @SerializedName("contenu")
    private String contenu;

    @SerializedName("created_at")
    private Date createdAt;

    @SerializedName("encadrant_nom")
    private String encadrantNom;  // Assurez-vous que ce champ existe

    // Getters and setters CORRECTS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProjetId() {
        return projetId;
    }

    public void setProjetId(int projetId) {
        this.projetId = projetId;
    }

    public int getEncadrantId() {
        return encadrantId;
    }

    public void setEncadrantId(int encadrantId) {
        this.encadrantId = encadrantId;
    }

    public String getContenu() {  // CORRECTION: getContenu() pas getContent()
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Date getCreatedAt() {  // CORRECTION: getCreatedAt() avec majuscule A
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getEncadrantNom() {
        return encadrantNom;
    }

    public void setEncadrantNom(String encadrantNom) {
        this.encadrantNom = encadrantNom;
    }
}