package com.example.Backend.controller;

import com.example.Backend.dto.EtudiantProfileDTO;
import com.example.Backend.service.UtilisateurService;
import com.example.Backend.security.JwtUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/etudiant")
@CrossOrigin(origins = "*")
public class EtudiantController {

    private final UtilisateurService utilisateurService;
    private final JwtUtil jwtUtil;

    public EtudiantController(UtilisateurService utilisateurService, JwtUtil jwtUtil) {
        this.utilisateurService = utilisateurService;
        this.jwtUtil = jwtUtil;
    }


    @GetMapping("/profile")
    public EtudiantProfileDTO getProfile(
            @RequestHeader("Authorization") String authHeader
    )
    {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token invalide");
        }
        String token = authHeader.substring(7);

        // ✅ Utiliser JwtUtil pour extraire l'email
        String email = jwtUtil.getEmailFromToken(token);

        // Retourner le profil complet
        return utilisateurService.getProfile(email);
    }
}
