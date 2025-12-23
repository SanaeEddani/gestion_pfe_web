package com.example.Backend.repository;

import com.example.Backend.model.Remarque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RemarqueRepository extends JpaRepository<Remarque, Integer> {
    List<Remarque> findByProjetIdOrderByCreatedAtDesc(Integer projetId);
}