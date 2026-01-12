package com.example.javafx2.logic;
public class VolCompletException extends VolException {
    public VolCompletException(int idVol) {
        super("Le vol " + idVol + " est complet. Aucun siège disponible");
    }
}