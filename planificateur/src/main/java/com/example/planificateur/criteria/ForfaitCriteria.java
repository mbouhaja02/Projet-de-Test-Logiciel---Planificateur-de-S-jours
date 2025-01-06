package com.example.planificateur.criteria;

import java.time.LocalDate;

public class ForfaitCriteria {
    private String cityFrom;
    private String cityTo;
    private LocalDate startDate;
    private int durationInDays;
    private double maxBudget;

    // Sous-critères
    private TransportCriteria transportCriteria;
    private HotelCriteria hotelCriteria;
    private ActivityCriteria activityCriteria;

    public ForfaitCriteria() {
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public int getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(int durationInDays) {
        this.durationInDays = durationInDays;
    }

    public double getMaxBudget() {
        return maxBudget;
    }

    public void setMaxBudget(double maxBudget) {
        this.maxBudget = maxBudget;
    }

    public TransportCriteria getTransportCriteria() {
        return transportCriteria;
    }

    public void setTransportCriteria(TransportCriteria transportCriteria) {
        this.transportCriteria = transportCriteria;
    }

    public HotelCriteria getHotelCriteria() {
        return hotelCriteria;
    }

    public void setHotelCriteria(HotelCriteria hotelCriteria) {
        this.hotelCriteria = hotelCriteria;
    }

    public ActivityCriteria getActivityCriteria() {
        return activityCriteria;
    }

    public void setActivityCriteria(ActivityCriteria activityCriteria) {
        this.activityCriteria = activityCriteria;
    }
}
