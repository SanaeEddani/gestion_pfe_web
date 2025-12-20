package com.example.frontend.model;

public class EncadrerRequest {
    private int projetId;
    private int encadrantId;

    public EncadrerRequest(int projetId, int encadrantId) {
        this.projetId = projetId;
        this.encadrantId = encadrantId;
    }

    public int getProjetId() { return projetId; }
    public int getEncadrantId() { return encadrantId; }
}
