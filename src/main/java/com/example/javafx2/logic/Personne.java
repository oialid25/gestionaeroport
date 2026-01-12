package com.example.javafx2.logic;
import java.io.Serializable;
import java.util.Date;
public abstract class Personne implements Serializable {
    private String num_passeport;
    private String nom;
    private String prenom;
    private String genre;
    private Date date_naissance;
    public String getNum_passeport() {
        return num_passeport;
    }
    public String getNom() {
        return nom;
    }
    public String getPrenom() {
        return prenom;
    }
    public String getGenre() {
        return genre;
    }
    public Date getDate_naissance() {
        return date_naissance;
    }
    public void setNum_passeport(String num_passeport){
        this.num_passeport=num_passeport;
    }
    public void setNom(String nom){
        this.nom=nom;
    }
    public void setPrenom(String prenom){
        this.prenom=prenom;
    }
    public void setGenre(String genre){
        this.genre=genre;
    }
    public void setDateNaissance(Date date){
        this.date_naissance=date;
    }
    @Override
    public String toString() {
        return
                "num_passeport=" + num_passeport +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", genre='" + genre + '\'' +
                ", date_naissance=" + date_naissance ;
    }
}
