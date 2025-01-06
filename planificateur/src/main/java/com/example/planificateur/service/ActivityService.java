package com.example.planificateur.service;

import com.example.planificateur.criteria.ActivityCriteria;
import com.example.planificateur.domain.Activity;

import java.time.LocalDate;
import java.util.List;

public interface ActivityService {
    List<Activity> findActivities(ActivityCriteria criteria,
                                  String city,
                                  LocalDate startDate,
                                  LocalDate endDate);
}
