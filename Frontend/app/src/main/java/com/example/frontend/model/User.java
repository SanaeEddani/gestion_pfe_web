package com.example.frontend.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    private int id;

    @SerializedName("nom")
    private String nom;

    @SerializedName("prenom")
    private String prenom;

    @SerializedName("email")
    private String email;

    @SerializedName("filiere")
    private String filiere;

    @SerializedName("role_id")
    private int roleId;

    @SerializedName("appogee_id")
    private Integer appogeeId;

    @SerializedName("code_prof_id")
    private Integer codeProfId;

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFiliere() { return filiere; }
    public void setFiliere(String filiere) { this.filiere = filiere; }

    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }

    public Integer getAppogeeId() { return appogeeId; }
    public void setAppogeeId(Integer appogeeId) { this.appogeeId = appogeeId; }

    public Integer getCodeProfId() { return codeProfId; }
    public void setCodeProfId(Integer codeProfId) { this.codeProfId = codeProfId; }
}