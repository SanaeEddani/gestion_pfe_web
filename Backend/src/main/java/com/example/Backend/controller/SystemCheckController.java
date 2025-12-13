package com.example.Backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Backend.repository.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/system")
@CrossOrigin(origins = "*")
public class SystemCheckController {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AppogeeRepository appogeeRepo;

    @Autowired
    private CodeProfRepository codeProfRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private UtilisateurRepository utilisateurRepo;

    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> systemCheck() {
        Map<String, Object> response = new HashMap<>();

        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            response.put("database_url", meta.getURL());
            response.put("database_product", meta.getDatabaseProductName());
            response.put("database_version", meta.getDatabaseProductVersion());
            response.put("driver_name", meta.getDriverName());
            response.put("connection", "success");
        } catch (Exception e) {
            response.put("connection", "failed");
            response.put("connection_error", e.getMessage());
        }

        try {
            // Vérifier les counts
            response.put("appogee_count", appogeeRepo.count());
            response.put("code_prof_count", codeProfRepo.count());
            response.put("roles_count", roleRepo.count());
            response.put("utilisateurs_count", utilisateurRepo.count());

            // Vérifier un apogee spécifique
            response.put("apogee_21000001_exists", appogeeRepo.findByNumAppogee("21000001") != null);
            response.put("code_prof_PROF2024001_exists", codeProfRepo.findByCodeProf("PROF2024001") != null);
            response.put("role_etudiant_exists", roleRepo.findByName("etudiant") != null);
            response.put("role_encadrant_exists", roleRepo.findByName("encadrant") != null);

            response.put("data_check", "success");

        } catch (Exception e) {
            response.put("data_check", "failed");
            response.put("data_check_error", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}