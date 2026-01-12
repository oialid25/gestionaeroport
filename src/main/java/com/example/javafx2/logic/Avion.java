package com.example.javafx2.logic;
import java.io.Serializable;
public class Avion implements Serializable {
    private String matricule;
    private double[] dimensions =new double[2];
    private double longueur_arret;
    private double temps_arret;
    private double longueur_depart;
    private double temps_depart;
    private double masse_supportee ;
    private int seat_capacite ;
    private String etat;
    private double[] coordonnees =new double[2];
    public Avion(String matricule,double dimensions_W,double dimensions_H,double longueur_arret, double temps_arret ,double longueur_depart,double temps_depart, double masse_supportee,int seat_capacite,String etat,double coordonnees_X,double coordonnees_Y) {
        this.matricule=matricule;
        this.dimensions[0]=dimensions_W;
        this.dimensions[1]=dimensions_H;
        this.longueur_arret=longueur_arret;
        this.temps_arret=temps_arret;
        this.longueur_depart=longueur_depart;
        this.temps_depart=temps_depart;
        this.masse_supportee=masse_supportee;
        this.seat_capacite=seat_capacite;
        this.etat=etat;
        this.coordonnees[0]=coordonnees_X;
        this.coordonnees[1]=coordonnees_Y;
    }
    public String getMatricule() {
        return this.matricule;
    }
    public double[] getDimensions() {
        return this.dimensions;
    }
    public double getMasse_supportee() {
        return this.masse_supportee;
    }
    public int getSeat_capacite() {
        return this.seat_capacite;
    }
    public String getEtat() {
        return this.etat;
    }
    public double getLongueur_arret() {
        return this.longueur_arret;
    }
    public double getLongueur_depart() {
        return this.longueur_depart;
    }
    public void setEtat(String etat) {
        this.etat = etat;
    }
    public void setCoordonnees(double x,double y) {
        this.coordonnees[0] = x;
        this.coordonnees[1] = y;
    }
    public double getTemps_arret() {
        return this.temps_arret;
    }
    public double getTemps_depart() {
        return this.temps_depart;
    }
    public double[] getCoordonnees() {
        return this.coordonnees;
    }
}
