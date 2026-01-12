package com.example.Backend.repository;

import com.example.Backend.model.Salle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalleRepository extends JpaRepository<Salle, Long> {
    // Avec JpaRepository, tu as déjà toutes les méthodes CRUD
    // findAll(), findById(), save(), delete(), etc.
}
