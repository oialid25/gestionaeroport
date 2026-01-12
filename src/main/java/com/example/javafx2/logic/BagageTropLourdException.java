package com.example.javafx2.logic;
public class BagageTropLourdException extends BagageException {
    public BagageTropLourdException(double masse, double masseMax) {
        super("Bagage trop lourd: " + masse + " kg. Maximum autorisé: " + masseMax + " kg");
    }
}