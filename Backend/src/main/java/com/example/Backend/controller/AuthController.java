package com.example.Backend.controller;

import com.example.Backend.dto.LoginRequest;
import com.example.Backend.model.Utilisateur;
import com.example.Backend.repository.UtilisateurRepository;
import com.example.Backend.service.AuthService;
import com.fasterxml.jackson.core.JsonPointer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    UtilisateurRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Chercher l'utilisateur pour récupérer le rôle

            Utilisateur user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));


            // Générer le token
            String token = authService.login(request.getEmail(), request.getPassword());

            // Préparer la réponse pour le frontend
            Map<String, Object> response = Map.of(
                    "token", token,
                    "role", user.getRole().getId() // ou getName() selon ce que tu veux envoyer
            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", e.getMessage()));
        }
    }


}


