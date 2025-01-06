package com.example.planificateur.repository;

import com.example.planificateur.domain.Activity;
import java.util.List;

public interface ActivityRepository {
    List<Activity> findAll();
}
