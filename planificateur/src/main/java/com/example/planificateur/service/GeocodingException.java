package com.example.planificateur.service;

public class GeocodingException extends Exception {
    public GeocodingException(String message) {
        super(message);
    }

    public GeocodingException(String message, Throwable cause) {
        super(message, cause);
    }
}