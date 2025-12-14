package com.example.frontend.model;

public class JwtResponse {
    private String token;
    private String role; // String pour correspondre au backend

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

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
