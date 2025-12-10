package edu.uit.pfeapp.model;

public class UserRequest {
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private String role;
    private String filiere;
    private String departement;
    private String codeProf;
    private String numAppogee;

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getFiliere() { return filiere; }
    public void setFiliere(String filiere) { this.filiere = filiere; }

    public String getDepartement() { return departement; }
    public void setDepartement(String departement) { this.departement = departement; }

    public String getCodeProf() { return codeProf; }
    public void setCodeProf(String codeProf) { this.codeProf = codeProf; }

    public String getNumAppogee() { return numAppogee; }
    public void setNumAppogee(String numAppogee) { this.numAppogee = numAppogee; }
}
