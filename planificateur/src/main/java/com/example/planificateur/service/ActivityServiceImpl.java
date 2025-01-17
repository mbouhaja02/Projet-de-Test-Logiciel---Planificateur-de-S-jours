package com.example.planificateur.service;

import com.example.planificateur.criteria.ActivityCriteria;
import com.example.planificateur.domain.Activity;
import com.example.planificateur.repository.ActivityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;

    public ActivityServiceImpl(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public List<Activity> findActivities(ActivityCriteria criteria, String city, LocalDate startDate, LocalDate endDate) {
        List<Activity> filteredActivities = activityRepository.findAll().stream()
                .filter(a -> a.getCity().equalsIgnoreCase(city))
                .filter(a -> !a.getDateTime().toLocalDate().isBefore(startDate) && !a.getDateTime().toLocalDate().isAfter(endDate))
                .filter(a -> criteria.getCategories().isEmpty() || criteria.getCategories().contains(a.getCategory().toLowerCase()))
                .collect(Collectors.toList());

        // Additional filtering to ensure only one activity per date
        Map<LocalDate, Activity> activityPerDate = new LinkedHashMap<>();
        for (Activity activity : filteredActivities) {
            LocalDate activityDate = activity.getDateTime().toLocalDate();
            if (!activityPerDate.containsKey(activityDate)) {
                activityPerDate.put(activityDate, activity);
            }
        }

        return new ArrayList<>(activityPerDate.values());
    }

}
