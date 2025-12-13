package com.example.Backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.*;

@RestController
@RequestMapping("/api/info")
@CrossOrigin(origins = "*")
public class DbInfoController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/connection")
    public ResponseEntity<Map<String, Object>> getConnectionInfo() {
        Map<String, Object> response = new HashMap<>();

        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();

            response.put("url", meta.getURL());
            response.put("database", conn.getCatalog());
            response.put("driver", meta.getDriverName());
            response.put("driver_version", meta.getDriverVersion());
            response.put("product", meta.getDatabaseProductName());
            response.put("product_version", meta.getDatabaseProductVersion());

            // Détection
            String url = meta.getURL();
            boolean isH2 = url.contains("h2") || url.contains("mem:") || url.contains("testdb");
            boolean isPostgres = url.contains("postgresql") || meta.getDatabaseProductName().contains("PostgreSQL");

            response.put("is_h2", isH2);
            response.put("is_postgresql", isPostgres);
            response.put("status", "connected");

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/tables")
    public ResponseEntity<Map<String, Object>> getTables() {
        Map<String, Object> response = new HashMap<>();

        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();

            // Récupérer toutes les tables
            List<Map<String, Object>> tables = new ArrayList<>();
            ResultSet rs = meta.getTables(null, null, "%", new String[]{"TABLE"});

            while (rs.next()) {
                Map<String, Object> table = new HashMap<>();
                table.put("name", rs.getString("TABLE_NAME"));
                table.put("type", rs.getString("TABLE_TYPE"));
                tables.add(table);
            }

            response.put("tables", tables);
            response.put("count", tables.size());
            response.put("status", "success");

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/count-appogee")
    public ResponseEntity<Map<String, Object>> countAppogee() {
        Map<String, Object> response = new HashMap<>();

        try (Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM appogee")) {

            if (rs.next()) {
                response.put("count", rs.getInt(1));
            }
            response.put("status", "success");

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/list-appogee")
    public ResponseEntity<Map<String, Object>> listAppogee() {
        Map<String, Object> response = new HashMap<>();

        try (Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, num_appogee FROM appogee ORDER BY id")) {

            List<Map<String, Object>> apogees = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> apogee = new HashMap<>();
                apogee.put("id", rs.getInt("id"));
                apogee.put("num_appogee", rs.getString("num_appogee"));
                apogees.add(apogee);
            }
            response.put("apogees", apogees);
            response.put("count", apogees.size());
            response.put("status", "success");

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}