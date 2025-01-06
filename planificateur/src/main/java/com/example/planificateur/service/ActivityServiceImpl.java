package com.example.planificateur.service;

import com.example.planificateur.criteria.ActivityCriteria;
import com.example.planificateur.domain.Activity;
import com.example.planificateur.repository.ActivityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;

    public ActivityServiceImpl(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public List<Activity> findActivities(ActivityCriteria criteria,
                                         String city,
                                         LocalDate startDate,
                                         LocalDate endDate) {
        List<Activity> all = activityRepository.findAll();

        return all.stream()
            // Filtrer sur la ville
            .filter(a -> a.getCity().equalsIgnoreCase(city))
            // Filtrer sur la date: a.getDateTime() doit être entre startDate et endDate
            .filter(a -> {
                LocalDate d = a.getDateTime().toLocalDate();
                return !d.isBefore(startDate) && !d.isAfter(endDate);
            })
            // Filtrer sur les catégories (si la liste n'est pas vide)
            .filter(a -> criteria.getCategories().isEmpty()
                      || criteria.getCategories().contains(a.getCategory().toLowerCase()))
            // la distance sera gérée plus tard via geocoding/distance
            .collect(Collectors.toList());
    }
}
