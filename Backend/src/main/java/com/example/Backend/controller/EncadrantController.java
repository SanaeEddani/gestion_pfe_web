package com.example.Backend.controller;

import com.example.Backend.dto.EtudiantProjetDTO;
import com.example.Backend.entity.Projet;
import com.example.Backend.model.Utilisateur;
import com.example.Backend.repository.ProjetRepository;
import com.example.Backend.repository.UtilisateurRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/encadrant")
@CrossOrigin(origins = "*")
public class EncadrantController {

    private final ProjetRepository projetRepository;
    private final UtilisateurRepository utilisateurRepository;

    public EncadrantController(
            ProjetRepository projetRepository,
            UtilisateurRepository utilisateurRepository
    ) {
        this.projetRepository = projetRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    /* ============================
       LISTE DES ÉTUDIANTS
       ============================ */
    @GetMapping("/etudiants")
    public List<EtudiantProjetDTO> getEtudiants(
            @RequestParam(required = false) String sujet
    ) {
        return projetRepository.findEtudiantsDisponibles(sujet);
    }

    /* ============================
       ENCADRER UN ÉTUDIANT
       ============================ */
    @PostMapping("/encadrer/{projetId}")
    public ResponseEntity<String> encadrer(
            @PathVariable Integer projetId,
            @RequestParam Integer encadrantId
    ) {
        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new RuntimeException("Projet introuvable"));

        projet.setEncadrantId(encadrantId);
        projetRepository.save(projet);

        return ResponseEntity.ok("Encadrement effectué");
    }

}
