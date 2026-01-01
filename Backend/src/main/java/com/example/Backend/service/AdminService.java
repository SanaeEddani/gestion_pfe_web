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
        projetRepository.findByEtudiant_Id(u.getId()).ifPresentOrElse(p -> {
            if (p.getEncadrant() != null) {
                dto.setAffecte(true);  // Affecté seulement si encadrant existe
                dto.setEncadrantNom(p.getEncadrant().getNom() + " " + p.getEncadrant().getPrenom());
            } else {
                dto.setAffecte(false); // Projet sans encadrant → pas affecté
                dto.setEncadrantNom(null);
            }
        }, () -> dto.setAffecte(false)); // Pas de projet → pas affecté

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

        // Récupérer le codeProf en String
        dto.setCodeProf(e.getCodeProf() != null ? e.getCodeProf().getCodeProf() : null);

        List<String> etudiants = e.getProjetsEncadres()
                .stream()
                .map(p -> p.getEtudiant().getNom() + " " + p.getEtudiant().getPrenom())
                .collect(Collectors.toList());


        dto.setEtudiants(etudiants);
        return dto;
    }



    /* ===================== AFFECTATION ===================== */

    public void affecter(AffectationRequestDTO request) {
        Utilisateur etudiant = utilisateurRepository.findById(request.getEtudiantId()).orElseThrow();
        Utilisateur encadrant = utilisateurRepository.findById(request.getEncadrantId()).orElseThrow();

        // Vérifier si un projet existe déjà
        Projet projet = projetRepository.findByEtudiant_Id(etudiant.getId())
                .orElseGet(() -> {
                    Projet p = new Projet();
                    p.setEtudiant(etudiant);
                    return p;
                });

        projet.setEncadrant(encadrant); // affecte l'étudiant à l'encadrant
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

    public void addStudentsToEncadrant(Long encadrantId, List<String> numAppogeeList) {
        Utilisateur encadrant = utilisateurRepository.findById(encadrantId).orElseThrow();

        for (String numAppogee : numAppogeeList) {
            Utilisateur etudiant = utilisateurRepository.findAll().stream()
                    .filter(u -> u.getAppogee() != null && numAppogee.equals(u.getAppogee().getNumAppogee()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Étudiant avec Apogée " + numAppogee + " introuvable"));

            // Vérifier si projet existe
            Projet projet = projetRepository.findByEtudiant_Id(etudiant.getId())
                    .orElseGet(() -> {
                        Projet p = new Projet();
                        p.setEtudiant(etudiant);
                        return p;
                    });

            projet.setEncadrant(encadrant);
            projetRepository.save(projet);
        }
    }

    public void removeStudentsFromEncadrant(Long encadrantId, List<String> numAppogeeList) {
        Utilisateur encadrant = utilisateurRepository.findById(encadrantId).orElseThrow();

        for (String numAppogee : numAppogeeList) {
            Utilisateur etudiant = utilisateurRepository.findAll().stream()
                    .filter(u -> u.getAppogee() != null && numAppogee.equals(u.getAppogee().getNumAppogee()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Étudiant avec Apogée " + numAppogee + " introuvable"));

            Projet projet = projetRepository.findByEtudiant_Id(etudiant.getId()).orElse(null);
            if (projet != null && projet.getEncadrant() != null && projet.getEncadrant().getId().equals(encadrantId)) {
                projet.setEncadrant(null);
                projetRepository.save(projet);
            }
        }
    }
    public DashboardStatsDTO getDashboardStats() {

        DashboardStatsDTO dto = new DashboardStatsDTO();

        // Étudiants
        List<Utilisateur> etudiants =
                utilisateurRepository.findByRole_Name("etudiant");

        long affectes = etudiants.stream()
                .filter(e ->
                        projetRepository.findByEtudiant_Id(e.getId())
                                .map(p -> p.getEncadrant() != null)
                                .orElse(false)
                ).count();

        dto.setTotalEtudiants(etudiants.size());
        dto.setEtudiantsAffectes((int) affectes);
        dto.setEtudiantsNonAffectes(etudiants.size() - (int) affectes);

        // Encadrants
        List<Utilisateur> encadrants =
                utilisateurRepository.findByRole_Name("encadrant");

        long avecEtudiants = encadrants.stream()
                .filter(e -> !e.getProjetsEncadres().isEmpty())
                .count();

        dto.setTotalEncadrants(encadrants.size());
        dto.setEncadrantsAvecEtudiants((int) avecEtudiants);
        dto.setEncadrantsSansEtudiants(encadrants.size() - (int) avecEtudiants);

        return dto;
    }


}
