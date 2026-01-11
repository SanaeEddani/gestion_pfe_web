package com.example.Backend.controller;

import com.example.Backend.dto.EtudiantProjetDTO;
import com.example.Backend.dto.admin.EtudiantProjetAdminDTO;
import com.example.Backend.dto.admin.SoutenanceDTO;
import com.example.Backend.model.Salle;
import com.example.Backend.model.Soutenance;
import com.example.Backend.service.SoutenanceService;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/soutenances")
public class SoutenanceController {

    private final SoutenanceService service;

    public SoutenanceController(SoutenanceService service) {
        this.service = service;
    }

    @GetMapping("/projets-eligibles")
    public List<EtudiantProjetAdminDTO> getProjetsEligibles() {
        return service.getProjetsEligibles();
    }

    @GetMapping
    public List<SoutenanceDTO> all() {
        return service.getAll();
    }

    @PostMapping
    public ResponseEntity<?> programmerSoutenance(@RequestBody SoutenanceDTO dto) {
        service.programmerSoutenance(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
