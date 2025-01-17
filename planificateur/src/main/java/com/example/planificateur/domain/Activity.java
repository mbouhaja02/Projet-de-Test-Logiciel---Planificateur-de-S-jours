package com.example.planificateur.domain;

import java.time.LocalDateTime;

public class Activity {
    private String address;
    private String city;
    private LocalDateTime dateTime;
    private String category; // e.g. "sport", "musique", etc.
    private double price;

    public Activity(String address, String city, LocalDateTime dateTime,
                    String category, double price) {
        this.address = address;
        this.city = city;
        this.dateTime = dateTime;
        this.category = category;
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }
}
