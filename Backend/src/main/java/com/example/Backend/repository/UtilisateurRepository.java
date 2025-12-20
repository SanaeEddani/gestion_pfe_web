package com.example.Backend.repository;

import com.example.Backend.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByEmail(String email);

    // AJOUTER CETTE LIGNE
    boolean existsByEmail(String email);
}
