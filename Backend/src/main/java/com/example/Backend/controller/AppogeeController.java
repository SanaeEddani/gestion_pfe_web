package com.example.Backend.controller;

import com.example.Backend.model.Appogee;
import com.example.Backend.repository.AppogeeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appogees")
public class AppogeeController {

    private final AppogeeRepository appogeeRepository;

    public AppogeeController(AppogeeRepository appogeeRepository) {
        this.appogeeRepository = appogeeRepository;
    }

    @GetMapping
    public List<Appogee> getAll() {
        return appogeeRepository.findAll();
    }

    @GetMapping("/{num}")
    public Appogee getByNum(@PathVariable String num) {
        return appogeeRepository.findByNumAppogee(num);
    }
}
