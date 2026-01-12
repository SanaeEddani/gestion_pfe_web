package com.example.Backend.service;

import com.example.Backend.model.Document;
import com.example.Backend.model.Utilisateur;
import com.example.Backend.repository.DocumentRepository;
import com.example.Backend.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final UtilisateurRepository utilisateurRepository;

    public DocumentService(DocumentRepository documentRepository, UtilisateurRepository utilisateurRepository) {
        this.documentRepository = documentRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    public Document uploadDocument(MultipartFile file, Long etudiantId) throws IOException {
        Utilisateur etudiant = utilisateurRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));

        String uploadDir = "uploads/";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        String filePath = uploadDir + file.getOriginalFilename();
        File dest = new File(filePath);
        file.transferTo(dest);

        Document document = new Document();
        document.setNomDocument(file.getOriginalFilename());
        document.setChemin(filePath);
        document.setFormat(getFileExtension(file.getOriginalFilename()));
        document.setEtudiant(etudiant);
        document.setDateUpload(LocalDateTime.now());

        return documentRepository.save(document);
    }

    private String getFileExtension(String fileName) {
        int i = fileName.lastIndexOf('.');
        if (i >= 0) return fileName.substring(i + 1);
        return "";
    }

    public List<Document> getDocumentsByEtudiant(Long etudiantId) {
        return documentRepository.findByEtudiantIdOrderByDateUploadDesc(etudiantId);
    }
}
