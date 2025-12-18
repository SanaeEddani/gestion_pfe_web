package com.example.Backend.repository;

import com.example.Backend.dto.EtudiantProjetDTO;
import com.example.Backend.entity.Projet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjetRepository extends JpaRepository<Projet, Integer> {

    @Query("""
        SELECT new com.example.Backend.dto.EtudiantProjetDTO(
            p.id,
            p.etudiantId,
            u.nom,
            u.prenom,
            u.filiere,
            p.sujet
        )
        FROM Projet p, Utilisateur u
        WHERE u.id = p.etudiantId
        AND p.encadrantId IS NULL
        AND (:sujet IS NULL OR p.sujet LIKE %:sujet%)
    """)
    List<EtudiantProjetDTO> findEtudiantsDisponibles(@Param("sujet") String sujet);
}
