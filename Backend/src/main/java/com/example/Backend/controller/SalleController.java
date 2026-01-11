package com.example.Backend.controller;

import com.example.Backend.model.Salle;
import com.example.Backend.repository.SalleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/salles")
public class SalleController {

    private final SalleRepository salleRepository;

    public SalleController(SalleRepository salleRepository) {
        this.salleRepository = salleRepository;
    }

    @GetMapping
    public List<Salle> getAllSalles() {
        return salleRepository.findAll();
    }
}
