package com.example.planificateur.domain;

import java.util.ArrayList;
import java.util.List;

public class Forfait {
    private Transport aller;
    private Transport retour;
    private Hotel hotel;
    private List<Activity> activities = new ArrayList<>();
    private double totalPrice;

    private List<String> errors = new ArrayList<>();

    public Forfait() {
    }

    public Transport getAller() {
        return aller;
    }

    public void setAller(Transport aller) {
        this.aller = aller;
    }

    public Transport getRetour() {
        return retour;
    }

    public void setRetour(Transport retour) {
        this.retour = retour;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

}
