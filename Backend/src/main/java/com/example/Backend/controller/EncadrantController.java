package com.example.Backend.controller;

import com.example.Backend.dto.EtudiantProjetDTO;
import com.example.Backend.model.Projet;
import com.example.Backend.service.EncadrantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/encadrant")
@CrossOrigin(origins = "*")
public class EncadrantController {

    private final EncadrantService encadrantService;

    public EncadrantController(EncadrantService encadrantService) {
        this.encadrantService = encadrantService;
    }

    /* =====================================================
       ÉTUDIANTS DISPONIBLES
       ===================================================== */
    @GetMapping("/etudiants")
    public List<EtudiantProjetDTO> getEtudiantsDisponibles() {

        return encadrantService.getEtudiantsDisponibles()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /* =====================================================
       MES ÉTUDIANTS
       ===================================================== */
    @GetMapping("/mes-etudiants")
    public List<EtudiantProjetDTO> getMesEtudiants(
            @RequestParam Long encadrantId
    ) {

        return encadrantService.getMesEtudiants(encadrantId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /* =====================================================
       ENCADRER (BOUTON)
       ===================================================== */
    public static class EncadrerRequest {
        public Long projetId;
        public Long encadrantId;
    }

    @PostMapping("/encadrer")
    public ResponseEntity<?> encadrer(
            @RequestBody EncadrerRequest req
    ) {

        if (req == null || req.projetId == null || req.encadrantId == null) {
            return ResponseEntity
                    .badRequest()
                    .body("projetId et encadrantId sont obligatoires");
        }

        encadrantService.encadrer(req.projetId, req.encadrantId);

        return ResponseEntity.ok().build();
    }

    /* =====================================================
       MAPPING PROJET -> DTO
       ===================================================== */
    private EtudiantProjetDTO mapToDTO(Projet p) {

        return new EtudiantProjetDTO(
                p.getId().intValue(),                          // projetId
                p.getEtudiant().getId().intValue(),            // etudiantId
                p.getEtudiant().getNom(),                      // nom
                p.getEtudiant().getPrenom(),                   // prenom
                p.getEtudiant().getFiliere(),                  // filiere
                p.getSujet(),                                  // sujet
                p.getEntreprise(),                             // entreprise
                p.getDateDebut(),                              // dateDebut
                p.getDateFin()                                 // dateFin
        );
    }
}
