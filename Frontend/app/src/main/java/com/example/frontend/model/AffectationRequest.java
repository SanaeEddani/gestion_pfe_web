package com.example.frontend.model;

public class AffectationRequest {
    public Long etudiantId;
    public Long encadrantId;

    public AffectationRequest(Long etudiantId, Long encadrantId) {
        this.etudiantId = etudiantId;
        this.encadrantId = encadrantId;
    }
}


