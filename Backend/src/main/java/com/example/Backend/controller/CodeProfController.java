package com.example.Backend.controller;

import com.example.Backend.model.CodeProf;
import com.example.Backend.repository.CodeProfRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/codeprofs")
public class CodeProfController {

    private final CodeProfRepository codeProfRepository;

    public CodeProfController(CodeProfRepository codeProfRepository) {
        this.codeProfRepository = codeProfRepository;
    }

    @GetMapping
    public List<CodeProf> getAll() {
        return codeProfRepository.findAll();
    }

    @GetMapping("/{code}")
    public CodeProf getByCode(@PathVariable String code) {
        return codeProfRepository.findByCodeProf(code);
    }
}
