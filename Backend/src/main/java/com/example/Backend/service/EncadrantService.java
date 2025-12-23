package com.example.Backend.service;

import com.example.Backend.model.Projet;
import com.example.Backend.model.Utilisateur;
import com.example.Backend.repository.ProjetRepository;
import com.example.Backend.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EncadrantService {

    private final ProjetRepository projetRepository;
    private final UtilisateurRepository utilisateurRepository;

    public EncadrantService(
            ProjetRepository projetRepository,
            UtilisateurRepository utilisateurRepository
    ) {
        this.projetRepository = projetRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    /* ===============================
       ÉTUDIANTS DISPONIBLES
       =============================== */
    public List<Projet> getEtudiantsDisponibles() {
        return projetRepository.findAll()
                .stream()
                .filter(p -> p.getEncadrant() == null)
                .collect(Collectors.toList());
    }

    /* ===============================
       MES ÉTUDIANTS
       =============================== */
    public List<Projet> getMesEtudiants(Long encadrantId) {
        return projetRepository.findAll()
                .stream()
                .filter(p ->
                        p.getEncadrant() != null &&
                                p.getEncadrant().getId().equals(encadrantId)
                )
                .collect(Collectors.toList());
    }

    /* ===============================
       ENCADRER UN ÉTUDIANT
       =============================== */
    public void encadrer(Long projetId, Long encadrantId) {

        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new RuntimeException("Projet introuvable"));

        if (projet.getEncadrant() != null) {
            throw new RuntimeException("Projet déjà encadré");
        }

        Utilisateur encadrant = utilisateurRepository.findById(encadrantId)
                .orElseThrow(() -> new RuntimeException("Encadrant introuvable"));

        projet.setEncadrant(encadrant);
        projetRepository.save(projet);
    }
}
