package com.example.javafx2.logic;
public class AvionIndisponibleException extends AvionException {
    public AvionIndisponibleException(String matricule) {
        super("L'avion " + matricule + " n'est pas disponible");
    }
}