package com.example.Backend.service;

import com.example.Backend.model.ResetPasswordToken;
import com.example.Backend.model.Utilisateur;
import com.example.Backend.repository.ResetPasswordTokenRepository;
import com.example.Backend.repository.UtilisateurRepository;
import com.example.Backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.mail.SimpleMailMessage;


import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UtilisateurRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ResetPasswordTokenRepository resetPasswordTokenRepository;


    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UtilisateurRepository utilisateurRepository;// déjà existant, juste pour rappel

    @Autowired
    private OtpService otpService;

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

    public void sendOtp(String email) {
        Utilisateur user = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        String otp = otpService.generateOtp(email);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Code OTP de réinitialisation");
        message.setText("Votre code OTP est : " + otp + "\nIl expire dans 5 minutes.");

        mailSender.send(message);
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




    public void resetPasswordWithOtp(String email, String otp, String newPassword) {
        boolean ok = otpService.validateOtp(email, otp);

        if (!ok) {
            throw new RuntimeException("OTP invalide ou expiré");
        }

        Utilisateur user = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        user.setPasswordHash(new BCryptPasswordEncoder().encode(newPassword));
        utilisateurRepository.save(user);
    }
}





