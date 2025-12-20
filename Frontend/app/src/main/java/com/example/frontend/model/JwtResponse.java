package com.example.frontend.model;

public class JwtResponse {
    private String token;
    private String role;
    private int userId; // AJOUTEZ CE CHAMP
    private String email; // Optionnel: ajoutez aussi l'email si disponible

    public JwtResponse() {
        // Constructeur par défaut
    }

    public JwtResponse(String token, String role, int userId) {
        this.token = token;
        this.role = role;
        this.userId = userId;
    }

    // Getters et setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public int getUserId() { return userId; } // AJOUTEZ
    public void setUserId(int userId) { this.userId = userId; } // AJOUTEZ

    public String getEmail() { return email; } // Optionnel
    public void setEmail(String email) { this.email = email; } // Optionnel

    // Méthode utilitaire pour récupérer role en int
    public int getRoleInt() {
        if (getRole() == null) return 0;
        switch (getRole().toLowerCase()) {
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