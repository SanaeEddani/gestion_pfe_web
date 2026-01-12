package com.example.Backend.service;

import com.example.Backend.dto.EtudiantProfileDTO;
import com.example.Backend.model.Utilisateur;
import com.example.Backend.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    public EtudiantProfileDTO getProfile(String email) {
        Utilisateur u = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        String numAppogee = u.getAppogee() != null ? u.getAppogee().getNumAppogee() : "";
        String roleName = u.getRole() != null ? u.getRole().getName() : "";

        return new EtudiantProfileDTO(
                u.getNom(),
                u.getPrenom(),
                u.getEmail(),
                u.getFiliere(),
                u.getDepartement(),
                numAppogee,
                roleName
        );
    }
}
