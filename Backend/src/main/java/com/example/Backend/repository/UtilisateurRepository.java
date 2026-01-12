package com.example.Backend.repository;

import com.example.Backend.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    // EXISTANT (login / signup)
    boolean existsByEmail(String email);
    Optional<Utilisateur> findByEmail(String email);

    // AJOUT POUR ADMIN (liste étudiants / encadrants)
    List<Utilisateur> findByRole_Name(String name);
}
