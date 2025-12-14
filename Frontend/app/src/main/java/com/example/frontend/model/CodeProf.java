package com.example.frontend.model;

public class CodeProf {
    private Long id;
    private String codeProf;

    public CodeProf() {}

    public CodeProf(Long id, String codeProf) {
        this.id = id;
        this.codeProf = codeProf;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodeProf() { return codeProf; }
    public void setCodeProf(String codeProf) { this.codeProf = codeProf; }
}
