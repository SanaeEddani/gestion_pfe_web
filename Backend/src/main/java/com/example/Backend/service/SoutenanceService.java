package com.example.Backend.service;

import com.example.Backend.dto.admin.EtudiantProjetAdminDTO;
import com.example.Backend.dto.admin.SoutenanceDTO;
import com.example.Backend.model.Projet;
import com.example.Backend.model.Soutenance;
import com.example.Backend.repository.ProjetRepository;
import com.example.Backend.repository.SoutenanceRepository;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class SoutenanceService {

    private final ProjetRepository projetRepo;
    private final SoutenanceRepository soutenanceRepo;

    public SoutenanceService(ProjetRepository projetRepo,
                             SoutenanceRepository soutenanceRepo) {
        this.projetRepo = projetRepo;
        this.soutenanceRepo = soutenanceRepo;
    }

    /* ================================
       1️⃣ PROJETS ÉLIGIBLES (≥ 4 mois)
       ================================ */
    public List<EtudiantProjetAdminDTO> getProjetsEligibles() {

        return projetRepo.findAll().stream()
                .filter(p ->
                        p.getDateDebut() != null &&
                                p.getDateFin() != null &&
                                ChronoUnit.MONTHS.between(
                                        p.getDateDebut(),
                                        p.getDateFin()
                                ) >= 4
                )
                .map(p -> {
                    EtudiantProjetAdminDTO dto = new EtudiantProjetAdminDTO();

                    dto.setId(p.getId());

                    if (p.getEtudiant() != null) {
                        dto.setNom(p.getEtudiant().getNom());
                        dto.setPrenom(p.getEtudiant().getPrenom());
                        dto.setFiliere(p.getEtudiant().getFiliere());
                    }

                    dto.setSujet(p.getSujet());
                    dto.setEntreprise(p.getEntreprise());

                    dto.setDateDebut(p.getDateDebut());
                    dto.setDateFin(p.getDateFin());

                    return dto;
                })
                .toList();
    }

    /* ================================
       2️⃣ SOUTENANCES DÉJÀ PROGRAMMÉES
       ================================ */
    public List<SoutenanceDTO> getAll() {

        return soutenanceRepo.findAll().stream()
                .map(s -> {
                    SoutenanceDTO dto = new SoutenanceDTO();

                    dto.id = s.getId();
                    dto.dateDebut = s.getDateHeureDebut().toString();
                    dto.dateFin = s.getDateHeureFin().toString();
                    dto.salle = s.getSalle() != null ? s.getSalle().getNom() : "";

                    if (s.getProjet() != null && s.getProjet().getEncadrant() != null) {
                        dto.nomEncadrant = s.getProjet().getEncadrant().getNom();
                        dto.departement = s.getProjet().getEncadrant().getDepartement();
                    }

                    dto.nombreEtudiants =
                            (s.getProjet() != null && s.getProjet().getEtudiant() != null) ? 1 : 0;

                    return dto;
                })
                .toList();
    }
}
