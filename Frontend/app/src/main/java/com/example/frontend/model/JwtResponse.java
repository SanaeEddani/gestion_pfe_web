package com.example.frontend.model;

public class JwtResponse {

    private String token;
    private String role; // admin / etudiant / encadrant
    private int id;      // ✅ ID utilisateur (AJOUT)

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

    public int getId() {            // ✅ NOUVEAU
        return id;
    }

    public void setId(int id) {     // ✅ NOUVEAU
        this.id = id;
    }

    // Méthode utilitaire : rôle en int
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
