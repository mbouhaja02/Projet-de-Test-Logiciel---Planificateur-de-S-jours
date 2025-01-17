package com.example.planificateur.domain;

import java.time.LocalDateTime;

/**
 * Représente un trajet d'une ville A vers une ville B,
 * dans un mode donné, avec des dates/horaires de départ et d'arrivée, et un prix.
 */
public class Transport {
    private String cityFrom;
    private String cityTo;
    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;
    private ModeTransport mode;
    private double price;

    public Transport(String cityFrom, String cityTo,
                     LocalDateTime departureDateTime,
                     LocalDateTime arrivalDateTime,
                     ModeTransport mode,
                     double price) {
        this.cityFrom = cityFrom;
        this.cityTo = cityTo;
        this.departureDateTime = departureDateTime;
        this.arrivalDateTime = arrivalDateTime;
        this.mode = mode;
        this.price = price;
    }

    public String getCityFrom() {
        return cityFrom;
    }

    public void setCityFrom(String cityFrom) {
        this.cityFrom = cityFrom;
    }

    public String getCityTo() {
        return cityTo;
    }

    public void setCityTo(String cityTo) {
        this.cityTo = cityTo;
    }

    public LocalDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public LocalDateTime getArrivalDateTime() {
        return arrivalDateTime;
    }

    public ModeTransport getMode() {
        return mode;
    }

    public double getPrice() {
        return price;
    }

}
