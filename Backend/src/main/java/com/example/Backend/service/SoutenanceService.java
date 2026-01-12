package com.example.Backend.service;

import com.example.Backend.dto.admin.EtudiantProjetAdminDTO;
import com.example.Backend.dto.admin.SoutenanceDTO;
import com.example.Backend.model.Projet;
import com.example.Backend.model.Salle;
import com.example.Backend.model.Soutenance;
import com.example.Backend.repository.ProjetRepository;
import com.example.Backend.repository.SalleRepository;
import com.example.Backend.repository.SoutenanceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class SoutenanceService {

    private final ProjetRepository projetRepo;
    private final SoutenanceRepository soutenanceRepo;
    private final SalleRepository salleRepo;

    public SoutenanceService(
            ProjetRepository projetRepo,
            SoutenanceRepository soutenanceRepo,
            SalleRepository salleRepo
    ) {
        this.projetRepo = projetRepo;
        this.soutenanceRepo = soutenanceRepo;
        this.salleRepo = salleRepo;
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

                    dto.projetId = s.getId();
                    dto.dateDebut = s.getDateHeureDebut().toString();
                    dto.dateFin = s.getDateHeureFin().toString();
                    dto.salle = s.getSalle() != null
                            ? s.getSalle().getNom()
                            : "";

                    if (s.getProjet() != null && s.getProjet().getEncadrant() != null) {
                        dto.nomEncadrant = s.getProjet().getEncadrant().getNom();
                        dto.departement = s.getProjet().getEncadrant().getDepartement();
                    }

                    dto.nombreEtudiants =
                            (s.getProjet() != null && s.getProjet().getEtudiant() != null)
                                    ? 1
                                    : 0;

                    return dto;
                })
                .toList();
    }

    /* ================================
       3️⃣ PROGRAMMER UNE SOUTENANCE
       ================================ */
    public void programmerSoutenance(SoutenanceDTO dto) {

        Projet projet = projetRepo.findById(dto.projetId)
                .orElseThrow(() ->
                        new RuntimeException("Projet introuvable"));

        Salle salle = salleRepo.findById(dto.salleId)
                .orElseThrow(() ->
                        new RuntimeException("Salle introuvable"));

        LocalDateTime debut = LocalDateTime.parse(dto.dateDebut);
        LocalDateTime fin = LocalDateTime.parse(dto.dateFin);

        if (fin.isBefore(debut)) {
            throw new RuntimeException("La date de fin doit être après la date de début");
        }

        Soutenance soutenance = new Soutenance();
        soutenance.setProjet(projet);
        soutenance.setSalle(salle);
        soutenance.setDateHeureDebut(debut);
        soutenance.setDateHeureFin(fin);

        soutenanceRepo.save(soutenance); // ✅ INSERT SQL RÉEL
    }
}
