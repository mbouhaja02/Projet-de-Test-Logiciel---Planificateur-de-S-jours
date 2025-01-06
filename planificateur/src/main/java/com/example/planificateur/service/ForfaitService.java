package com.example.planificateur.service;

import com.example.planificateur.criteria.ForfaitCriteria;
import com.example.planificateur.domain.Forfait;

import java.util.List;

public interface ForfaitService {
    List<Forfait> createForfaits(ForfaitCriteria criteria);
}
