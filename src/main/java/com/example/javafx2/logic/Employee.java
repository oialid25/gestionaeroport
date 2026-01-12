package com.example.javafx2.logic;
import java.util.Date;
public class Employee extends Personne {
    String role;
    Vol vol;
    public Employee(String num_passeport, String nom, String prenom, String genre, Date date_naissance,Vol vol,String role){
        this.setNum_passeport(num_passeport);
        this.setNom(nom);
        this.setPrenom(prenom);
        this.setGenre(genre);
        this.setDateNaissance(date_naissance);;
        this.vol=vol;
        this.role=role;
    }
    public String getRole() {
        return this.role;
    }
    public Vol getVol() {
        return this.vol;
    }
    public void setVol(Vol vol) {
        this.vol = vol;
    }
}
