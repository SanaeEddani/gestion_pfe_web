package com.example.Backend.repository;

import com.example.Backend.model.CodeProf;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeProfRepository extends JpaRepository<CodeProf, Long> {
    CodeProf findByCodeProf(String codeProf);
}