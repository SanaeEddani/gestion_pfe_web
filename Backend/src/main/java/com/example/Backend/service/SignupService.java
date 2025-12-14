package com.example.Backend.service;

import com.example.Backend.dto.SignupRequest;
import com.example.Backend.model.*;
import com.example.Backend.repository.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SignupService {

    private final UtilisateurRepository utilRepo;
    private final AppogeeRepository appRepo;
    private final CodeProfRepository profRepo;
    private final RoleRepository roleRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public SignupService(UtilisateurRepository u,
                       AppogeeRepository a,
                       CodeProfRepository p,
                       RoleRepository r) {
        this.utilRepo = u;
        this.appRepo = a;
        this.profRepo = p;
        this.roleRepo = r;
    }

    @Transactional
    public String signup(SignupRequest req) {

        try {
            /* =======================
               VALIDATIONS DE BASE
               ======================= */
            if (req.getNom() == null || req.getNom().trim().isEmpty()) {
                return "Nom obligatoire";
            }

            if (req.getPrenom() == null || req.getPrenom().trim().isEmpty()) {
                return "Prénom obligatoire";
            }

            if (req.getEmail() == null || req.getEmail().trim().isEmpty()) {
                return "Email obligatoire";
            }

            if (req.getPassword() == null || req.getPassword().trim().isEmpty()) {
                return "Mot de passe obligatoire";
            }

            if (req.getRole() == null || req.getRole().trim().isEmpty()) {
                return "Rôle obligatoire";
            }

            /* =======================
               NORMALISATION
               ======================= */
            String nom = req.getNom().trim().toLowerCase();
            String prenom = req.getPrenom().trim().toLowerCase();
            String email = req.getEmail().trim().toLowerCase();
            String role = req.getRole().trim().toLowerCase();

            /* =======================
               VALIDATION EMAIL UIT
               ======================= */
            String expectedEmail = prenom + "." + nom + "@uit.ac.ma";
            if (!email.equals(expectedEmail)) {
                return "Email invalide. Format requis : prenom.nom@uit.ac.ma";
            }

            /* =======================
               EMAIL DÉJÀ EXISTANT
               ======================= */
            if (utilRepo.existsByEmail(email)) {
                return "Vous avez déjà un compte. Veuillez vous connecter.";
            }

            /* =======================
               RÔLE
               ======================= */
            Role roleEntity = roleRepo.findByName(role);
            if (roleEntity == null) {
                return "Rôle invalide (etudiant ou encadrant)";
            }

            /* =======================
               CRÉATION UTILISATEUR
               ======================= */
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setNom(req.getNom().trim());
            utilisateur.setPrenom(req.getPrenom().trim());
            utilisateur.setEmail(email);
            utilisateur.setPasswordHash(encoder.encode(req.getPassword()));
            utilisateur.setRole(roleEntity);

            /* =======================
               ÉTUDIANT
               ======================= */
            if ("etudiant".equals(role)) {

                if (req.getAppogee() == null || req.getAppogee().trim().isEmpty()) {
                    return "Numéro d'apogée obligatoire";
                }

                String numAppogee = req.getAppogee().trim();
                Appogee appogee = appRepo.findByNumAppogee(numAppogee);

                if (appogee == null) {
                    List<Appogee> nativeResults = appRepo.findNativeByNumAppogee(numAppogee);
                    if (!nativeResults.isEmpty()) {
                        appogee = nativeResults.get(0);
                    }
                }

                if (appogee == null) {
                    return "Numéro d'apogée invalide";
                }

                Appogee finalAppogee = appogee;
                boolean apogeeUsed = utilRepo.findAll().stream()
                        .anyMatch(u -> u.getAppogee() != null &&
                                u.getAppogee().getId().equals(finalAppogee.getId()));

                if (apogeeUsed) {
                    return "Ce numéro d'apogée est déjà associé à un compte";
                }

                utilisateur.setAppogee(appogee);
                utilisateur.setFiliere(req.getFiliere() != null ? req.getFiliere().trim() : "");

            }
            /* =======================
               ENCADRANT
               ======================= */
            else if ("encadrant".equals(role)) {

                if (req.getCodeProf() == null || req.getCodeProf().trim().isEmpty()) {
                    return "Code professeur obligatoire";
                }

                String codeProf = req.getCodeProf().trim();
                CodeProf codeProfEntity = profRepo.findByCodeProf(codeProf);

                if (codeProfEntity == null) {
                    return "Code professeur invalide";
                }

                boolean codeUsed = utilRepo.findAll().stream()
                        .anyMatch(u -> u.getCodeProf() != null &&
                                u.getCodeProf().getId().equals(codeProfEntity.getId()));

                if (codeUsed) {
                    return "Ce code professeur est déjà associé à un compte";
                }

                utilisateur.setCodeProf(codeProfEntity);
                utilisateur.setDepartement(req.getDepartement() != null ? req.getDepartement().trim() : "");
            }

            /* =======================
               SAUVEGARDE
               ======================= */
            utilRepo.save(utilisateur);
            return "Inscription réussie";

        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur serveur. Veuillez réessayer.";
        }
    }
}
