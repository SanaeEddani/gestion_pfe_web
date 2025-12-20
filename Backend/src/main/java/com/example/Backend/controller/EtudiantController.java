package com.example.Backend.controller;

import com.example.Backend.dto.EtudiantProfileDTO;
import com.example.Backend.service.UtilisateurService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/etudiant")
@CrossOrigin(origins = "*")
public class EtudiantController {

    private final UtilisateurService utilisateurService;

    public EtudiantController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/profile")
    public EtudiantProfileDTO getProfile(@RequestHeader("Authorization") String authHeader) {
        // authHeader = "Bearer <token>"
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token invalide");
        }
        String token = authHeader.substring(7);

        // Ici tu peux décoder le JWT ou vérifier le token pour récupérer l'email
        // Pour l'exemple, supposons que le token est directement l'email :
        String email = token;

        return utilisateurService.getProfile(email);
    }
}
