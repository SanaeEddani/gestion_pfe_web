package com.example.Backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Backend.repository.*;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/api/check")
@CrossOrigin(origins = "*")
public class DbCheckController {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AppogeeRepository appogeeRepo;

    @GetMapping("/appogee-count")
    public ResponseEntity<Map<String, Object>> getAppogeeCount() {
        Map<String, Object> response = new HashMap<>();

        try {
            // Méthode 1: JPA
            long jpaCount = appogeeRepo.count();

            // Méthode 2: Native SQL
            try (Connection conn = dataSource.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM appogee")) {

                rs.next();
                long nativeCount = rs.getLong(1);

                // Méthode 3: Lister tous les apogees
                List<Map<String, String>> apogees = new ArrayList<>();
                try (ResultSet rs2 = stmt.executeQuery("SELECT id, num_appogee FROM appogee ORDER BY id")) {
                    while (rs2.next()) {
                        Map<String, String> apogee = new HashMap<>();
                        apogee.put("id", String.valueOf(rs2.getInt("id")));
                        apogee.put("num_appogee", rs2.getString("num_appogee"));
                        apogees.add(apogee);
                    }
                }

                response.put("jpa_count", jpaCount);
                response.put("native_count", nativeCount);
                response.put("apogees", apogees);
                response.put("apogee_list_size", apogees.size());
                response.put("status", "success");

            }

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search-apogee/{num}")
    public ResponseEntity<Map<String, Object>> searchApogee(@PathVariable String num) {
        Map<String, Object> response = new HashMap<>();

        try (Connection conn = dataSource.getConnection()) {

            // Recherche exacte
            String exactQuery = "SELECT id, num_appogee FROM appogee WHERE num_appogee = ?";
            List<Map<String, String>> exactResults = new ArrayList<>();

            try (PreparedStatement pstmt = conn.prepareStatement(exactQuery)) {
                pstmt.setString(1, num);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    Map<String, String> apogee = new HashMap<>();
                    apogee.put("id", String.valueOf(rs.getInt("id")));
                    apogee.put("num_appogee", rs.getString("num_appogee"));
                    exactResults.add(apogee);
                }
            }

            // Recherche avec LIKE
            String likeQuery = "SELECT id, num_appogee FROM appogee WHERE num_appogee LIKE ?";
            List<Map<String, String>> likeResults = new ArrayList<>();

            try (PreparedStatement pstmt = conn.prepareStatement(likeQuery)) {
                pstmt.setString(1, "%" + num + "%");
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    Map<String, String> apogee = new HashMap<>();
                    apogee.put("id", String.valueOf(rs.getInt("id")));
                    apogee.put("num_appogee", rs.getString("num_appogee"));
                    likeResults.add(apogee);
                }
            }

            response.put("num_recherche", num);
            response.put("exact_results", exactResults);
            response.put("like_results", likeResults);
            response.put("exact_count", exactResults.size());
            response.put("like_count", likeResults.size());
            response.put("status", "success");

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}