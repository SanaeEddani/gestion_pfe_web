package com.example.Backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "code_prof")
public class CodeProf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code_prof", unique = true, nullable = false)
    private String codeProf;

    public CodeProf() {}

    public CodeProf(String codeProf) {
        this.codeProf = codeProf;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodeProf() { return codeProf; }
    public void setCodeProf(String codeProf) { this.codeProf = codeProf; }
}