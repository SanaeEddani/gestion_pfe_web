package com.example.Backend.service;

import com.example.Backend.model.Utilisateur;
import com.example.Backend.repository.UtilisateurRepository;
import com.example.Backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UtilisateurRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public String login(String email, String password) {

        System.out.println("DEBUG - Connexion pour : " + email);

        // Recherche de l'utilisateur
        Utilisateur user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        String hashStocke = user.getPasswordHash();

        if (hashStocke == null || hashStocke.isEmpty()) {
            throw new RuntimeException("Mot de passe non défini pour cet utilisateur");
        }

        // Vérification du mot de passe
        boolean passwordOk = false;
        try {
            passwordOk = passwordEncoder.matches(password, hashStocke);
        } catch (Exception e) {
            System.out.println("⚠️ Exception lors de la vérification du mot de passe : " + e.getMessage());
        }

        if (!passwordOk) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        // Génération du token JWT
        return jwtUtil.generateToken(user.getEmail(), user.getRole().getName());
    }

    /**
     * Méthode utilitaire pour tester BCrypt
     */
    public void debugPasswordHash(String password) {
        for (int i = 1; i <= 3; i++) {
            String hash = passwordEncoder.encode(password);
            System.out.println("Hash " + i + " : " + hash + " | matches ? " + passwordEncoder.matches(password, hash));
        }
    }
}
