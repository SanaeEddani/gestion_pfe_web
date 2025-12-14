package com.example.Backend.controller;

import com.example.Backend.model.Role;
import com.example.Backend.repository.RoleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    @GetMapping("/{name}")
    public Role getByName(@PathVariable String name) {
        return roleRepository.findByName(name);
    }
}
