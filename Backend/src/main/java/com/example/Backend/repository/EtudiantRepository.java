package com.example.Backend.repository;

import com.example.Backend.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EtudiantRepository extends JpaRepository<Utilisateur, Integer> {

    @Query("SELECT u FROM Utilisateur u WHERE u.role.id = 2") // 2 = étudiant
    List<Utilisateur> findAllEtudiants();

    @Query("SELECT u FROM Utilisateur u WHERE u.id = :id AND u.role.id = 2")
    Optional<Utilisateur> findEtudiantById(@Param("id") Integer id);

    Optional<Utilisateur> findByEmail(String email);
}