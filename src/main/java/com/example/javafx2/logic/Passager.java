package com.example.javafx2.logic;
import java.util.Date;
public class  Passager extends Personne{
    private Vol vol;
    private double masse_bag;
    public Passager(String num_passeport,String nom,String prenom,String genre,Date date_naissance,Vol vol,double masse_bag ) throws VolCompletException {
        vol.seatDispo();
        this.vol=vol;
        this.setNum_passeport(num_passeport);
        this.setNom(nom);
        this.setPrenom(prenom);
        this.setGenre(genre);
        this.setDateNaissance(date_naissance);
        this.masse_bag=masse_bag;
        vol.ajouter_passager(this);
    }
    public Vol getVol(){
        return vol;
    }
    public double masse_bag(){
        return masse_bag;
    }
    public void setVol(Vol vol){
        this.vol=vol;
    }
    public void setMasseBag(double masse_bag){
        this.masse_bag=masse_bag;
    }
    public boolean verifier_bag() throws BagageTropLourdException {
        if (this.masse_bag  < this.vol.getAvion().getMasse_supportee()) {
            return true;
        } 
        throw new BagageTropLourdException(this.masse_bag,this.vol.getAvion().getMasse_supportee());
    }
}
