package com.example.Backend.controller;

import com.example.Backend.dto.ProjetDTO;
import com.example.Backend.model.Projet;
import com.example.Backend.security.JwtUtil;
import com.example.Backend.service.ProjetService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projets")
@CrossOrigin(origins = "*")
public class ProjetController {

    private final ProjetService projetService;
    private final JwtUtil jwtUtil;

    public ProjetController(ProjetService projetService, JwtUtil jwtUtil) {
        this.projetService = projetService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public Projet ajouterProjet(@RequestHeader("Authorization") String authHeader,
                                @RequestBody ProjetDTO dto) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token invalide");
        }
        String token = authHeader.substring(7);
        String emailEtudiant = jwtUtil.getEmailFromToken(token);

        return projetService.ajouterProjet(emailEtudiant, dto);
    }

    @GetMapping("/me")
    public List<ProjetDTO> getMesProjets(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token invalide");
        }
        String token = authHeader.substring(7);
        String emailEtudiant = jwtUtil.getEmailFromToken(token);

        return projetService.getProjetsByEtudiantEmail(emailEtudiant);
    }
}
