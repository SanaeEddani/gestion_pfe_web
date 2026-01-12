package com.example.Backend.repository;

import com.example.Backend.model.Projet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjetRepository extends JpaRepository<Projet, Long> {

    // ✅ Version Optional (1 projet max par étudiant)
    Optional<Projet> findByEtudiant_Id(Long etudiantId);

    // ✅ Version List (si plusieurs projets possibles)
    List<Projet> findByEtudiantId(Long etudiantId);

    long countByEncadrant_Id(Long encadrantId);


    // Option 3 : projets éligibles >= 4 mois (requête native MySQL)
    @Query(value = "SELECT * FROM projets " +
            "WHERE date_debut IS NOT NULL AND date_fin IS NOT NULL " +
            "AND TIMESTAMPDIFF(MONTH, date_debut, date_fin) >= 4", nativeQuery = true)
    List<Projet> findProjetsEligibles();

}
