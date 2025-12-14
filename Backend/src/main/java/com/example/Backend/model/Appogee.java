package com.example.Backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "appogee", schema = "public")
public class Appogee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "num_appogee", unique = true, nullable = false)
    private String numAppogee;

    public Appogee() {}

    public Appogee(String numAppogee) {
        this.numAppogee = numAppogee;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumAppogee() { return numAppogee; }
    public void setNumAppogee(String numAppogee) { this.numAppogee = numAppogee; }
}