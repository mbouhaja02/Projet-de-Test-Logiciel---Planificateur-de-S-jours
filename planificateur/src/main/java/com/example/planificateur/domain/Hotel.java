package com.example.planificateur.domain;

public class Hotel {
    private String address;
    private String city;
    private int stars;          // 1 à 5
    private double pricePerNight;

    public Hotel() {
    }

    public Hotel(String address, String city, int stars, double pricePerNight) {
        this.address = address;
        this.city = city;
        this.stars = stars;
        this.pricePerNight = pricePerNight;
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

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }
}
