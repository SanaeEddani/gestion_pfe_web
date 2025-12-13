package edu.uit.pfeapp.model;

public class Appogee {
    private Long id;
    private String numAppogee;

    public Appogee() {}

    public Appogee(Long id, String numAppogee) {
        this.id = id;
        this.numAppogee = numAppogee;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumAppogee() { return numAppogee; }
    public void setNumAppogee(String numAppogee) { this.numAppogee = numAppogee; }
}
