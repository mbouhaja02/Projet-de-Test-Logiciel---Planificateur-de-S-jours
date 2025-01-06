package com.example.planificateur.criteria;

import java.util.ArrayList;
import java.util.List;

public class ActivityCriteria {
    private List<String> categories = new ArrayList<>(); // e.g. ["sport", "musique"]
    private Double maxDistance; // km, null = pas de limite

    public ActivityCriteria() {
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public Double getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(Double maxDistance) {
        this.maxDistance = maxDistance;
    }
}
