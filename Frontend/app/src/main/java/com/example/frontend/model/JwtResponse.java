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
        try {
            return Integer.parseInt(getRole());
        } catch (NumberFormatException e) {
            return 0; // rôle inconnu
        }
    }
}
