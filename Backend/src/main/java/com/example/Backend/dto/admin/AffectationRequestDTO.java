package com.example.Backend.dto.admin;

public class AffectationRequestDTO {

    private Long etudiantId;
    private Long encadrantId;

    public Long getEtudiantId() {
        return etudiantId;
    }

    public void setEtudiantId(Long etudiantId) {
        this.etudiantId = etudiantId;
    }

    public Long getEncadrantId() {
        return encadrantId;
    }

    public void setEncadrantId(Long encadrantId) {
        this.encadrantId = encadrantId;
    }
}
