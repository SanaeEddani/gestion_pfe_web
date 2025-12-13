package com.example.Backend.controller;

import com.example.Backend.model.Utilisateur;
import com.example.Backend.repository.UtilisateurRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/utilisateurs")
public class UtilisateurController {

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurController(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @GetMapping
    public List<Utilisateur> getAll() {
        return utilisateurRepository.findAll();
    }

    @GetMapping("/exists/{email}")
    public boolean existsByEmail(@PathVariable String email) {
        return utilisateurRepository.existsByEmail(email);
    }
}
