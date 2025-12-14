package com.example.Backend.controller;

import com.example.Backend.dto.LoginRequest;
import com.example.Backend.dto.SignupRequest;
import com.example.Backend.model.Utilisateur;
import com.example.Backend.repository.UtilisateurRepository;
import com.example.Backend.service.AuthService;
import com.example.Backend.service.SignupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;
    private final SignupService signupService;
    private final UtilisateurRepository userRepository;

    public AuthController(AuthService authService,
                          SignupService signupService,
                          UtilisateurRepository userRepository) {
        this.authService = authService;
        this.signupService = signupService;
        this.userRepository = userRepository;
    }

    /* =========================
       LOGIN
       ========================= */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Utilisateur user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

            String token = authService.login(request.getEmail(), request.getPassword());

            Map<String, Object> response = Map.of(
                    "token", token,
                    "role", user.getRole().getName() // ou getId() si tu veux
            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    /* =========================
       REGISTER (SignupService)
       ========================= */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody SignupRequest req) {

        Map<String, Object> response = new HashMap<>();

        try {
            String result = signupService.signup(req);

            boolean success = result.equalsIgnoreCase("Inscription réussie");

            response.put("success", success);
            response.put("message", result);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur serveur : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /* =========================
       TEST
       ========================= */
    @GetMapping("/test")
    public String test() {
        return "Backend is working!";
    }
}
