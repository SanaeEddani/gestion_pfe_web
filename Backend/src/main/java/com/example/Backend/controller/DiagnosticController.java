package com.example.Backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Backend.repository.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/diagnostic")
@CrossOrigin(origins = "*")
public class DiagnosticController {

    @Autowired
    private AppogeeRepository appogeeRepo;

    @Autowired
    private CodeProfRepository codeProfRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private UtilisateurRepository utilisateurRepo;

    @GetMapping("/simple-check")
    public ResponseEntity<Map<String, Object>> simpleCheck() {
        Map<String, Object> response = new HashMap<>();

        try {
            // Vérifier les comptes
            response.put("appogee_count", appogeeRepo.count());
            response.put("code_prof_count", codeProfRepo.count());
            response.put("roles_count", roleRepo.count());
            response.put("utilisateurs_count", utilisateurRepo.count());

            response.put("status", "success");

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}