package com.example.Backend.service;

import com.example.Backend.dto.ProjetDTO;
import com.example.Backend.model.Projet;
import com.example.Backend.model.Utilisateur;
import com.example.Backend.repository.ProjetRepository;
import com.example.Backend.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjetService {

    private final ProjetRepository projetRepository;
    private final UtilisateurRepository utilisateurRepository;

    public ProjetService(ProjetRepository projetRepository, UtilisateurRepository utilisateurRepository) {
        this.projetRepository = projetRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    public Projet ajouterProjet(String emailEtudiant, ProjetDTO dto) {
        Utilisateur etudiant = utilisateurRepository.findByEmail(emailEtudiant)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));

        Projet projet = new Projet();
        projet.setEtudiant(etudiant);
        projet.setSujet(dto.getSujet());
        projet.setDescription(dto.getDescription());
        projet.setEntreprise(dto.getEntreprise());

        return projetRepository.save(projet);
    }

    public List<ProjetDTO> getProjetsByEtudiantEmail(String emailEtudiant) {
        Utilisateur etudiant = utilisateurRepository.findByEmail(emailEtudiant)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));

        List<Projet> projets = projetRepository.findByEtudiantId(etudiant.getId());

        return projets.stream()
                .map(projet -> new ProjetDTO(
                        projet.getSujet(),
                        projet.getDescription(),
                        projet.getEntreprise()
                ))
                .collect(Collectors.toList());
    }
}
