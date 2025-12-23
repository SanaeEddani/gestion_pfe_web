package com.example.Backend.controller;

import com.example.Backend.model.Utilisateur;
import com.example.Backend.repository.UtilisateurRepository;
import org.springframework.security.core.Authentication; // <-- à ne pas oublier
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UtilisateurController {

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurController(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @GetMapping("/utilisateur/profile")
    public Utilisateur getProfile(Authentication authentication) {
        String email = authentication.getName(); // récupéré depuis JWT
        return utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
}
