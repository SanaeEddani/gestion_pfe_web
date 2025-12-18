package com.example.Backend.service;

import com.example.Backend.dto.admin.*;
import com.example.Backend.model.Projet;
import com.example.Backend.model.Utilisateur;
import com.example.Backend.repository.ProjetRepository;
import com.example.Backend.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final UtilisateurRepository utilisateurRepository;
    private final ProjetRepository projetRepository;

    public AdminService(UtilisateurRepository utilisateurRepository,
                        ProjetRepository projetRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.projetRepository = projetRepository;
    }

    /* ===================== ÉTUDIANTS ===================== */

    public List<StudentAdminDTO> getAllStudents() {
        return utilisateurRepository.findByRole_Name("etudiant")
                .stream()
                .map(this::mapToStudentDTO)
                .collect(Collectors.toList());
    }

    private StudentAdminDTO mapToStudentDTO(Utilisateur u) {
        StudentAdminDTO dto = new StudentAdminDTO();
        dto.setId(u.getId());
        dto.setNom(u.getNom());
        dto.setPrenom(u.getPrenom());
        dto.setEmail(u.getEmail());
        dto.setFiliere(u.getFiliere());

        // Apogée
        if (u.getAppogee() != null) {
            dto.setApogee(u.getAppogee().getNumAppogee());
        }

        // Affectation
        boolean affecte = projetRepository.findByEtudiant_Id(u.getId()).isPresent();
        dto.setAffecte(affecte);

        projetRepository.findByEtudiant_Id(u.getId()).ifPresent(p ->
                dto.setEncadrantNom(p.getEncadrant().getNom() + " " + p.getEncadrant().getPrenom())
        );

        return dto;
    }


    /* ===================== ENCADRANTS ===================== */

    public List<EncadrantAdminDTO> getAllEncadrants() {
        return utilisateurRepository.findByRole_Name("encadrant")
                .stream()
                .map(this::mapToEncadrantDTO)
                .collect(Collectors.toList());
    }

    private EncadrantAdminDTO mapToEncadrantDTO(Utilisateur e) {
        EncadrantAdminDTO dto = new EncadrantAdminDTO();
        dto.setId(e.getId());
        dto.setNom(e.getNom());
        dto.setPrenom(e.getPrenom());
        dto.setEmail(e.getEmail());
        dto.setDepartement(e.getDepartement());

        List<String> etudiants = e.getProjetsEncadres()
                .stream()
                .map(p -> p.getEtudiant().getNom())
                .collect(Collectors.toList());

        dto.setEtudiants(etudiants);
        return dto;
    }

    /* ===================== AFFECTATION ===================== */

    public void affecter(AffectationRequestDTO request) {
        Utilisateur etudiant = utilisateurRepository.findById(request.getEtudiantId()).orElseThrow();
        Utilisateur encadrant = utilisateurRepository.findById(request.getEncadrantId()).orElseThrow();

        Projet projet = new Projet();
        projet.setEtudiant(etudiant);
        projet.setEncadrant(encadrant);

        projetRepository.save(projet);
    }

    public void reaffecter(AffectationRequestDTO request) {
        Projet projet = projetRepository.findByEtudiant_Id(request.getEtudiantId()).orElseThrow();
        Utilisateur newEncadrant = utilisateurRepository.findById(request.getEncadrantId()).orElseThrow();
        projet.setEncadrant(newEncadrant);
        projetRepository.save(projet);
    }

    public void supprimerAffectation(Long etudiantId) {
        Projet projet = projetRepository.findByEtudiant_Id(etudiantId).orElseThrow();
        projetRepository.delete(projet);
    }
}
