package com.example.Backend.controller;

import com.example.Backend.dto.EtudiantProjetDTO;
import com.example.Backend.repository.ProjetRepository;
import com.example.Backend.repository.UtilisateurRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    /* =====================================================
       MÉTHODE UTILITAIRE : Object[] -> DTO
       ===================================================== */
    private EtudiantProjetDTO mapToDTO(Object[] row) {

        return new EtudiantProjetDTO(
                ((Number) row[0]).intValue(),   // projetId
                ((Number) row[1]).intValue(),   // etudiantId
                (String) row[2],                // nom
                (String) row[3],                // prenom
                (String) row[4],                // filiere
                (String) row[5],                // sujet
                (String) row[6],                // entreprise
                row[7] != null ? ((Date) row[7]).toLocalDate() : null,
                row[8] != null ? ((Date) row[8]).toLocalDate() : null
        );
    }

    /* =====================================================
       ÉTUDIANTS DISPONIBLES
       ===================================================== */
    @GetMapping("/etudiants")
    public List<EtudiantProjetDTO> getEtudiantsDisponibles(
            @RequestParam(required = false) String filiere
    ) {
        return projetRepository
                .findEtudiantsDisponiblesRaw(filiere)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /* =====================================================
       MES ÉTUDIANTS
       ===================================================== */
    @GetMapping("/mes-etudiants")
    public List<EtudiantProjetDTO> getMesEtudiants(
            @RequestParam Integer encadrantId
    ) {
        return projetRepository
                .findMesEtudiantsRaw(encadrantId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /* =====================================================
       ENCADRER (BOUTON)
       ===================================================== */
    public static class EncadrerRequest {
        public Integer projetId;
        public Integer encadrantId;
    }

    @PostMapping("/encadrer")
    public ResponseEntity<?> encadrer(
            @RequestBody EncadrerRequest req
    ) {

        // 🔴 Sécurité : données manquantes
        if (req == null || req.projetId == null || req.encadrantId == null) {
            return ResponseEntity
                    .badRequest()
                    .body("projetId et encadrantId sont obligatoires");
        }

        // 🔴 Sécurité : encadrant inexistant
        if (!utilisateurRepository.existsById(req.encadrantId.longValue())) {

            return ResponseEntity
                    .badRequest()
                    .body("Encadrant inexistant");
        }

        int updated = projetRepository.encadrerEtudiant(
                req.projetId,
                req.encadrantId
        );

        if (updated == 1) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity
                .status(409)
                .body("Projet déjà encadré ou introuvable");
    }
}
