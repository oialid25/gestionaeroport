package com.example.javafx2.logic;
public class AeroportException extends Exception {
    public AeroportException(String message) {
        super(message);
    }
    public AeroportException(String message, Throwable cause) {
        super(message, cause);
    }
}