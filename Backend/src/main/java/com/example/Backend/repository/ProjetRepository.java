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
        u.id,
        u.nom,
        u.prenom,
        u.filiere,
        p.sujet,
        p.entreprise,
        p.date_debut,
        p.date_fin
    )
    FROM Projet p
    JOIN Utilisateur u ON u.id = p.etudiantId
    WHERE p.encadrantId IS NULL
    AND (:filiere IS NULL OR u.filiere = :filiere)
""")
    List<EtudiantProjetDTO> findEtudiantsDisponibles(
            @Param("filiere") String filiere
    );


}
