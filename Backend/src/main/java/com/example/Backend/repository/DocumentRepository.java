package com.example.Backend.repository;

import com.example.Backend.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Integer> {
    List<Document> findByEtudiantIdOrderByDateUploadDesc(Long etudiantId);
}
