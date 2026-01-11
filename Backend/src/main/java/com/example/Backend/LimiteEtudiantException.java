package com.example.Backend;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // Renvoie 400 au lieu de 500
public class LimiteEtudiantException extends RuntimeException {
    public LimiteEtudiantException(String message) {
        super(message);
    }
}