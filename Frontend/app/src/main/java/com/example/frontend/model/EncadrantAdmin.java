package com.example.frontend.model;

import java.util.List;

public class EncadrantAdmin {

    public Long id;
    public String nom;
    public String prenom;
    public String email;
    public String codeProf;   // <-- ajouté
    public String departement;
    // 🔴 IMPORTANT : List<String> et NON StudentAdmin
    public List<String> etudiants;


    public Long getId() {
        return id;
    }

}
