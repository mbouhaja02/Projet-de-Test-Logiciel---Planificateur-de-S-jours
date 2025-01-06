package com.example.planificateur.domain;

import java.time.LocalDateTime;

public class Activity {
    private String address;
    private String city;
    private LocalDateTime dateTime;
    private String category; // e.g. "sport", "musique", etc.
    private double price;

    public Activity() {
    }

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

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
