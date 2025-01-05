package com.example.planificateur.criteria;

import com.example.planificateur.domain.ModeTransport;

/**
 * Critères permettant de filtrer/ordonner les transports
 * (mode préféré, tri par prix, tri par durée).
 */
public class TransportCriteria {

    private ModeTransport preferredMode;  // TRAIN, AVION, ou null si sans préférence
    private boolean prioritizeCheapest;   // tri par prix
    private boolean prioritizeShortest;   // tri par durée

    public TransportCriteria() {
    }

    public TransportCriteria(ModeTransport preferredMode,
                             boolean prioritizeCheapest,
                             boolean prioritizeShortest) {
        this.preferredMode = preferredMode;
        this.prioritizeCheapest = prioritizeCheapest;
        this.prioritizeShortest = prioritizeShortest;
    }

    public ModeTransport getPreferredMode() {
        return preferredMode;
    }

    public void setPreferredMode(ModeTransport preferredMode) {
        this.preferredMode = preferredMode;
    }

    public boolean isPrioritizeCheapest() {
        return prioritizeCheapest;
    }

    public void setPrioritizeCheapest(boolean prioritizeCheapest) {
        this.prioritizeCheapest = prioritizeCheapest;
    }

    public boolean isPrioritizeShortest() {
        return prioritizeShortest;
    }

    public void setPrioritizeShortest(boolean prioritizeShortest) {
        this.prioritizeShortest = prioritizeShortest;
    }
}
