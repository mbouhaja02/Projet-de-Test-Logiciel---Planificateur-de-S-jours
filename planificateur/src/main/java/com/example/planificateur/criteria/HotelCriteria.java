package com.example.planificateur.criteria;

public class HotelCriteria {
    private int minStars;               // 0 = pas de préférence
    private boolean prioritizeCheapest;
    private boolean prioritizeMaxStars;

    public HotelCriteria() {
    }

    public int getMinStars() {
        return minStars;
    }

    public void setMinStars(int minStars) {
        this.minStars = minStars;
    }

    public boolean isPrioritizeCheapest() {
        return prioritizeCheapest;
    }

    public void setPrioritizeCheapest(boolean prioritizeCheapest) {
        this.prioritizeCheapest = prioritizeCheapest;
    }

    public boolean isPrioritizeMaxStars() {
        return prioritizeMaxStars;
    }

    public void setPrioritizeMaxStars(boolean prioritizeMaxStars) {
        this.prioritizeMaxStars = prioritizeMaxStars;
    }
}
