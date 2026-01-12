package com.example.javafx2.logic;
public class PisteNonDisponibleException extends PisteException {
    public PisteNonDisponibleException() {
        super("Aucune piste disponible pour cette opération");
    }
}