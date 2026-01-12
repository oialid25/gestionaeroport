package com.example.javafx2.logic;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class gestion_aeroport {
    private List<Avion> avions = new ArrayList();
    private List<Vol> vols = new ArrayList();
    private List<Piste> pistes = new ArrayList();
    public gestion_aeroport() {
    }
    public void planifier(Vol var1) throws AvionIndisponibleException {
        if (var1.getAvion() == null || var1.getId_vol() == 0) {
            String matricule = var1.getAvion() != null ? var1.getAvion().getMatricule() : "inconnu";
            throw new AvionIndisponibleException(matricule);
        }
        for (Vol v : this.vols) {
            if (v.getAvion() != null && v.getAvion().getMatricule().equals(var1.getAvion().getMatricule())) {
                throw new AvionIndisponibleException(var1.getAvion().getMatricule());
            }
        }
        this.vols.add(var1);
        try {
            FileOutputStream var2 = new FileOutputStream("Vols.ser");
            ObjectOutputStream var3 = new ObjectOutputStream(var2);
            var3.writeObject(this.vols);
            var3.close();
            var2.close();
        } catch (Exception var4) {
            System.out.println("Erreur lors de la sauvegarde: " + var4.getMessage());
        }
    }

    public void ajouter_vol(Vol var1) {
        this.vols.add(var1);
        try {
            FileOutputStream var2 = new FileOutputStream("Vols.ser");
            ObjectOutputStream var3 = new ObjectOutputStream(var2);
            var3.writeObject(this.vols);
            var3.close();
            var2.close();
        } catch (Exception var4) {
            System.out.println("Erreur lors de la sauvegarde: " + var4.getMessage());
        }
    }

    public void ajouter_avion(Avion var1) {
        this.avions.add(var1);
    }

    public void ajouter_piste(Piste var1) {
        this.pistes.add(var1);
    }

    public Piste autoriser_descente_(Vol var1) throws PisteNonDisponibleException {
        for (Piste p : this.pistes) {
            p.verifierEtMettreDisponible();
        }
        Iterator var2 = this.pistes.iterator();
        Piste var3 = null;
        while (var2.hasNext()) {
            Piste pisteCandidate = (Piste)var2.next();
            pisteCandidate.verifierEtMettreDisponible();
            if ("escale".equals(pisteCandidate.getType()) && pisteCandidate.getEst_dispo() && 
                var1.getAvion().getDimensions()[0] <= pisteCandidate.getDimensions()[0] && 
                var1.getAvion().getDimensions()[1] <= pisteCandidate.getDimensions()[1]) {
                var3 = pisteCandidate;
                break;
            }
        }
        if (var3 == null) {
            throw new PisteNonDisponibleException();
        }
        var3.occuper(var1.getAvion().getTemps_depart());
        var1.getAvion().setCoordonnees(var3.getCoordonnees()[0], var3.getCoordonnees()[1]);
        return var3;
    }
    public Piste Parker(Vol var1) {
        for (Piste p : this.pistes) {
            p.verifierEtMettreDisponible();
        }
        Piste var2 = null;
        Iterator var3 = this.pistes.iterator();
        Piste var4;
        while(var3.hasNext()) {
            var4 = (Piste)var3.next();
            if (var4.getCoordonnees()[0] == var1.getAvion().getCoordonnees()[0] && 
                var4.getCoordonnees()[1] == var1.getAvion().getCoordonnees()[1]) {
                var4.disponible();
                break;
            }
        }
        var3 = this.pistes.iterator();
        while(var3.hasNext()) {
            var4 = (Piste)var3.next();
            var4.verifierEtMettreDisponible();
            if (var4.getEst_dispo() && var4.getType().equals("parking") && 
                var1.getAvion().getDimensions()[0] <= var4.getDimensions()[0] && 
                var1.getAvion().getDimensions()[1] <= var4.getDimensions()[1]) {
                var4.occuper(0.0);
                var1.getAvion().setCoordonnees(var4.getCoordonnees()[0], var4.getCoordonnees()[1]);
                var2 = var4;
                break;
            }
        }
        return var2;
    }

    public Piste autoriser_depart(Vol var1) {
        for (Piste p : this.pistes) {
            p.verifierEtMettreDisponible();
        }
        Piste var2 = null;
        Iterator var3 = this.pistes.iterator();
        Piste var4;
        while(var3.hasNext()) {
            var4 = (Piste)var3.next();
            if (var4.getCoordonnees()[0] == var1.getAvion().getCoordonnees()[0] && 
                var4.getCoordonnees()[1] == var1.getAvion().getCoordonnees()[1]) {
                var4.disponible();
                break;
            }
        }
        var3 = this.pistes.iterator();
        while(var3.hasNext()) {
            var4 = (Piste)var3.next();
            var4.verifierEtMettreDisponible();
            if (var4.getEst_dispo() && var4.getType().equals("escale") && 
                var1.getAvion().getDimensions()[0] <= var4.getDimensions()[0] && 
                var1.getAvion().getDimensions()[1] <= var4.getDimensions()[1]) {
                var4.occuper(var1.getAvion().getTemps_depart());
                var1.getAvion().setCoordonnees(var4.getCoordonnees()[0], var4.getCoordonnees()[1]);
                var2 = var4;
                break;
            }
        }
        return var2;
    }

    public List<Vol> getVols() {
        return this.vols;
    }

    public List<Avion> getAvions() {
        return this.avions;
    }

    public List<Piste> getPistes() {
        return this.pistes;
    }

    public void afficherTableauDesvol() {
        try {
            FileInputStream var1 = new FileInputStream("Vols.ser");
            ObjectInputStream var2 = new ObjectInputStream(var1);
            List var3 = (List)var2.readObject();
            System.out.println("\n=== VOLS DU JOUR ===");
            boolean var4 = true;
            Iterator var5 = var3.iterator();
            while(var5.hasNext()) {
                Vol var6 = (Vol)var5.next();
                if (var6.getDate_vol() != null) {
                    LocalDate var7 = var6.getDate_vol().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    if (var7.isEqual(LocalDate.now())) {
                        PrintStream var10000 = System.out;
                        int var10001 = var6.getId_vol();
                        var10000.println("Vol ID: " + var10001 + " | Date de depart: " + String.valueOf(var6.getDate_vol()) + " | Seat occupee: " + var6.getSeat_occupee() + " | Seat capacite: " + var6.getAvion().getSeat_capacite() + " | Matricule: " + var6.getAvion().getMatricule() + " | Depart: " + var6.getDepart() + " | Destination: " + var6.getDestination());
                        var4 = false;
                    }
                }
            }
            if (var4) {
                System.out.println("Aucun vol prevu pour aujourd'hui.");
            }
            var2.close();
            var1.close();
        } catch (Exception var8) {
            System.out.println("Erreur lors de la lecture: " + var8.getMessage());
        }
    }
}
