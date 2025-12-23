package com.example.Backend.repository;

import com.example.Backend.dto.EtudiantProjetDTO;
import com.example.Backend.entity.Projet;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ProjetRepository extends JpaRepository<Projet, Integer> {

    /* ===============================
       ÉTUDIANTS DISPONIBLES
       =============================== */
    @Query(
            value = """
        SELECT
            p.id           AS projetId,
            u.id           AS etudiantId,
            u.nom          AS nom,
            u.prenom       AS prenom,
            u.filiere      AS filiere,
            p.sujet        AS sujet,
            p.entreprise   AS entreprise,
            p.date_debut   AS dateDebut,
            p.date_fin     AS dateFin
        FROM projets p
        JOIN utilisateurs u ON u.id = p.etudiant_id
        WHERE p.encadrant_id IS NULL
          AND u.role_id = 2
          AND (:filiere IS NULL OR :filiere = '' OR u.filiere = :filiere)
        """,
            nativeQuery = true
    )
    List<Object[]> findEtudiantsDisponiblesRaw(
            @Param("filiere") String filiere
    );

    /* ===============================
       MES ÉTUDIANTS
       =============================== */
    @Query(value = """
    SELECT
        p.id            AS projet_id,
        u.id            AS etudiant_id,
        u.nom,
        u.prenom,
        u.filiere,
        p.sujet,
        p.entreprise,
        p.date_debut,
        p.date_fin
    FROM projets p
    JOIN utilisateurs u ON u.id = p.etudiant_id
    WHERE p.encadrant_id = :encadrantId
    """, nativeQuery = true)
    List<Object[]> findMesEtudiantsRaw(
            @Param("encadrantId") Integer encadrantId
    );

    /* ===============================
       ENCADRER UN ÉTUDIANT
       =============================== */
    @Modifying
    @Transactional
    @Query(
            value = """
        UPDATE projets
        SET encadrant_id = :encadrantId
        WHERE id = :projetId
          AND encadrant_id IS NULL
        """,
            nativeQuery = true
    )
    int encadrerEtudiant(
            @Param("projetId") Integer projetId,
            @Param("encadrantId") Integer encadrantId
    );

    /* ===============================
       RECHERCHE PROJET PAR ÉTUDIANT
       (ajouté depuis l’autre version)
       =============================== */
    Optional<Projet> findByEtudiant_Id(Integer etudiantId);
}
