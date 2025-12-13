package com.example.Backend.controller;

import com.example.Backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/debug")
@CrossOrigin(origins = "*")
public class DebugController {

    @Autowired
    private AppogeeRepository appogeeRepo;

    @Autowired
    private CodeProfRepository codeProfRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private UtilisateurRepository utilisateurRepo;

    @GetMapping("/apogees")
    public ResponseEntity<Map<String, Object>> getAllApogees() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Map<String, String>> apogees = new ArrayList<>();
            appogeeRepo.findAll().forEach(a -> {
                Map<String, String> apogee = new HashMap<>();
                apogee.put("id", String.valueOf(a.getId()));
                apogee.put("numAppogee", a.getNumAppogee());
                apogees.add(apogee);
            });

            response.put("count", apogees.size());
            response.put("apogees", apogees);
            response.put("success", true);

        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/check/{num}")
    public ResponseEntity<Map<String, Object>> checkApogee(@PathVariable String num) {
        Map<String, Object> response = new HashMap<>();

        response.put("numRecherche", num);
        response.put("numRechercheTrim", num.trim());

        // Méthode 1: findByNumAppogee
        var found = appogeeRepo.findByNumAppogee(num);
        response.put("findByNumAppogee_found", found != null);
        if (found != null) {
            response.put("found_id", found.getId());
            response.put("found_num", found.getNumAppogee());
        }

        // Méthode 2: Native query
        try {
            var nativeResults = appogeeRepo.findNativeByNumAppogee(num);
            response.put("nativeResults_count", nativeResults.size());
            if (!nativeResults.isEmpty()) {
                response.put("native_first_id", nativeResults.get(0).getId());
                response.put("native_first_num", nativeResults.get(0).getNumAppogee());
            }
        } catch (Exception e) {
            response.put("native_error", e.getMessage());
        }

        // Tous les apogees pour comparaison
        List<String> allNums = new ArrayList<>();
        List<Map<String, String>> allApogees = new ArrayList<>();
        appogeeRepo.findAll().forEach(a -> {
            allNums.add(a.getNumAppogee());
            Map<String, String> ap = new HashMap<>();
            ap.put("id", String.valueOf(a.getId()));
            ap.put("num", a.getNumAppogee());
            allApogees.add(ap);
        });

        response.put("allApogees_count", allNums.size());
        response.put("allApogees", allApogees);
        response.put("success", true);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/test-connection")
    public ResponseEntity<Map<String, Object>> testConnection() {
        Map<String, Object> response = new HashMap<>();

        try {
            response.put("appogee_count", appogeeRepo.count());
            response.put("code_prof_count", codeProfRepo.count());
            response.put("roles_count", roleRepo.count());
            response.put("utilisateurs_count", utilisateurRepo.count());
            response.put("connection", "OK");
            response.put("success", true);
        } catch (Exception e) {
            response.put("connection", "ERROR");
            response.put("error", e.getMessage());
            response.put("success", false);
        }

        return ResponseEntity.ok(response);
    }
}