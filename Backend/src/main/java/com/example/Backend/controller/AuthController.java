package com.example.Backend.controller;

import com.example.Backend.dto.SignupRequest;
import com.example.Backend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService a) {
        this.authService = a;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody SignupRequest req) {
        Map<String, Object> response = new HashMap<>();
        try {
            String result = authService.signup(req);
            boolean success = result.equals("Inscription réussie");
            response.put("success", success);
            response.put("message", result);
            return ResponseEntity.ok(response); // TOUJOURS OK
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur serveur: " + e.getMessage());
            return ResponseEntity.status(500).body(response); // seulement pour vraie exception
        }
    }


    @GetMapping("/test")
    public String test() {
        return "Backend is working!";
    }
}
