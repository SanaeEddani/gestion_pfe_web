package com.example.Backend.controller;

import com.example.Backend.dto.admin.*;
import com.example.Backend.service.AdminService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/students")
    public List<StudentAdminDTO> students() {
        return adminService.getAllStudents();
    }

    @GetMapping("/encadrants")
    public List<EncadrantAdminDTO> encadrants() {
        return adminService.getAllEncadrants();
    }

    @PostMapping("/affecter")
    public void affecter(@RequestBody AffectationRequestDTO dto) {
        adminService.affecter(dto);
    }

    @PutMapping("/reaffecter")
    public void reaffecter(@RequestBody AffectationRequestDTO dto) {
        adminService.reaffecter(dto);
    }

    @DeleteMapping("/desaffecter/{etudiantId}")
    public void desaffecter(@PathVariable Long etudiantId) {
        adminService.supprimerAffectation(etudiantId);
    }

    // Ajouter plusieurs étudiants à un encadrant
    @PostMapping("/encadrants/{encadrantId}/addStudents")
    public void addStudentsToEncadrant(@PathVariable Long encadrantId, @RequestBody List<String> numAppogeeList) {
        adminService.addStudentsToEncadrant(encadrantId, numAppogeeList);
    }

    // Supprimer plusieurs étudiants d’un encadrant
    @PostMapping("/encadrants/{encadrantId}/removeStudents")
    public void removeStudentsFromEncadrant(@PathVariable Long encadrantId, @RequestBody List<String> numAppogeeList) {
        adminService.removeStudentsFromEncadrant(encadrantId, numAppogeeList);
    }

    @GetMapping("/dashboard")
    public DashboardStatsDTO dashboardStats() {
        return adminService.getDashboardStats();
    }


}
