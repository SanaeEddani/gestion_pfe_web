package com.example.Backend.dto.admin;

public class DashboardStatsDTO {

    private int totalEtudiants;
    private int etudiantsAffectes;
    private int etudiantsNonAffectes;

    private int totalEncadrants;
    private int encadrantsAvecEtudiants;
    private int encadrantsSansEtudiants;

    // ===== GETTERS =====

    public int getTotalEtudiants() {
        return totalEtudiants;
    }

    public int getEtudiantsAffectes() {
        return etudiantsAffectes;
    }

    public int getEtudiantsNonAffectes() {
        return etudiantsNonAffectes;
    }

    public int getTotalEncadrants() {
        return totalEncadrants;
    }

    public int getEncadrantsAvecEtudiants() {
        return encadrantsAvecEtudiants;
    }

    public int getEncadrantsSansEtudiants() {
        return encadrantsSansEtudiants;
    }

    // ===== SETTERS =====

    public void setTotalEtudiants(int totalEtudiants) {
        this.totalEtudiants = totalEtudiants;
    }

    public void setEtudiantsAffectes(int etudiantsAffectes) {
        this.etudiantsAffectes = etudiantsAffectes;
    }

    public void setEtudiantsNonAffectes(int etudiantsNonAffectes) {
        this.etudiantsNonAffectes = etudiantsNonAffectes;
    }

    public void setTotalEncadrants(int totalEncadrants) {
        this.totalEncadrants = totalEncadrants;
    }

    public void setEncadrantsAvecEtudiants(int encadrantsAvecEtudiants) {
        this.encadrantsAvecEtudiants = encadrantsAvecEtudiants;
    }

    public void setEncadrantsSansEtudiants(int encadrantsSansEtudiants) {
        this.encadrantsSansEtudiants = encadrantsSansEtudiants;
    }
}
