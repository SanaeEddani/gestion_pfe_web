package com.example.Backend.repository;

import com.example.Backend.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    boolean existsByEmail(String email);
}