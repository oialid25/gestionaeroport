package com.example.javafx2.logic;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class Vol implements Serializable {
  static int compteur;
  private int seat_occupee;
  private int id_vol;
  private Avion avion;
  private List<Employee> employees= new ArrayList<Employee>();
  private List<Passager> passagers= new ArrayList<Passager>();
  private String destination;
  private String depart;
  private Date date_vol;
  private int duree_vol;
  public Avion getAvion() {
        return avion;
    }
  public int getSeat_occupee() {
        return seat_occupee;
    }
  public Vol(Avion avion, List<Employee> employees, List<Passager>passagers, String destination, String depart, Date date_vol, int duree_vol){
    if ("disponible".equals(avion.getEtat())){
      this.avion=avion;
      this.employees=employees;
      this.passagers=passagers;
      this.destination=destination;
      this.depart=depart;
      this.date_vol=date_vol;
      this.duree_vol=duree_vol;
      this.seat_occupee=passagers!=null ? passagers.size() : 0;
      this.id_vol=++compteur;
      avion.setEtat("envol");
    }
  }
  public void ajouter_passager(Passager passager){
      this.passagers.add(passager);
      this.seat_occupee++;
    }
  public void retirer_passager(Passager passager){
      if (this.passagers.remove(passager)) {
          this.seat_occupee--;
      }
    }
  public int getId_vol() {
      return id_vol;
  }
  public String getDestination() {
      return destination;
  }
  public String getDepart() {
      return depart;
  }
  public Date getDate_vol() {
      return date_vol;
  }
  public int getDuree_vol() {
      return duree_vol;
  }
  public List<Employee> getEmployees() {
      return employees;
  }
  public List<Passager> getPassagers() {
      return passagers;
  }
  public void ajouter_employee(Employee employee){
    this.employees.add(employee);
  }
  public boolean seatDispo() throws VolCompletException {
    if (this.seat_occupee<this.avion.getSeat_capacite()){
      return true;
    }
    throw new VolCompletException(this.id_vol);
  }
}
