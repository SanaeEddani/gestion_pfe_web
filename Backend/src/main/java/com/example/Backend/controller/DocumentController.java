package com.example.Backend.controller;

import com.example.Backend.model.Document;
import com.example.Backend.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "*")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Document> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("etudiantId") Long etudiantId
    ) {
        try {
            Document doc = documentService.uploadDocument(file, etudiantId);
            return ResponseEntity.ok(doc);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<List<Document>> getDocuments(@PathVariable Long etudiantId) {
        List<Document> docs = documentService.getDocumentsByEtudiant(etudiantId);
        return ResponseEntity.ok(docs);
    }
}
