package com.example.frontend.model;

public class JwtResponse {

    private String token;
    private String role;     // admin / etudiant / encadrant

    // ⚠️ On garde LES DEUX pour compatibilité
    private int id;          // utilisé dans une partie du code
    private int userId;      // utilisé dans une autre partie

    private String email;    // optionnel

    /* =========================
       Constructeurs
       ========================= */

    public JwtResponse() {
        // constructeur par défaut
    }

    public JwtResponse(String token, String role, int userId) {
        this.token = token;
        this.role = role;
        this.userId = userId;
        this.id = userId; // synchro automatique
    }

    /* =========================
       Getters / Setters
       ========================= */

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /* --- ID (version 1) --- */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        this.userId = id; // synchro
    }

    /* --- userId (version 2) --- */
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
        this.id = userId; // synchro
    }

    /* --- Email (optionnel) --- */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /* =========================
       Méthode utilitaire
       ========================= */

    public int getRoleInt() {
        if (role == null) return 0;

        switch (role.toLowerCase()) {
            case "admin":
                return 1;
            case "etudiant":
                return 2;
            case "encadrant":
                return 3;
            default:
                return 0;
        }
    }
}
