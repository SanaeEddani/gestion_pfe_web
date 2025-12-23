package com.example.Backend.repository;

import com.example.Backend.model.Projet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjetRepository extends JpaRepository<Projet, Long> {

    Optional<Projet> findByEtudiant_Id(Long etudiantId);

}
