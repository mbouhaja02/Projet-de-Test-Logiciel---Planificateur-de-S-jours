package com.example.planificateur;

import com.example.planificateur.criteria.ActivityCriteria;
import com.example.planificateur.domain.Activity;
import com.example.planificateur.repository.ActivityRepository;
import com.example.planificateur.service.ActivityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActivityServiceImplTest {

    @Mock
    private ActivityRepository activityRepository;

    private ActivityServiceImpl activityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        activityService = new ActivityServiceImpl(activityRepository);
    }

    @Test
    void findActivities_shouldFilterByCity() {
        // Arrange
        ActivityCriteria criteria = new ActivityCriteria();
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 1, 10);
        List<Activity> activities = Arrays.asList(
                new Activity("123 Main St", "Paris", LocalDateTime.of(2025, 1, 5, 10, 0), "sport", 50.0),
                new Activity("456 Elm St", "Lyon", LocalDateTime.of(2025, 1, 5, 10, 0), "sport", 75.0)
        );
        when(activityRepository.findAll()).thenReturn(activities);

        // Act
        List<Activity> result = activityService.findActivities(criteria, "Paris", startDate, endDate);

        // Assert
        assertEquals(1, result.size());
        assertEquals("123 Main St", result.get(0).getAddress());
    }

    @Test
    void findActivities_shouldFilterByDate() {
        // Arrange
        ActivityCriteria criteria = new ActivityCriteria();
        LocalDate startDate = LocalDate.of(2025, 1, 5);
        LocalDate endDate = LocalDate.of(2025, 1, 7);
        List<Activity> activities = Arrays.asList(
                new Activity("123 Main St", "Paris", LocalDateTime.of(2025, 1, 4, 10, 0), "sport", 50.0),
                new Activity("456 Elm St", "Paris", LocalDateTime.of(2025, 1, 6, 10, 0), "sport", 75.0),
                new Activity("789 Oak St", "Paris", LocalDateTime.of(2025, 1, 8, 10, 0), "sport", 60.0)
        );
        when(activityRepository.findAll()).thenReturn(activities);

        // Act
        List<Activity> result = activityService.findActivities(criteria, "Paris", startDate, endDate);

        // Assert
        assertEquals(1, result.size());
        assertEquals("456 Elm St", result.get(0).getAddress());
    }

    @Test
    void findActivities_shouldFilterByCategory() {
        // Arrange
        ActivityCriteria criteria = new ActivityCriteria();
        criteria.setCategories(Arrays.asList("sport", "musique"));
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 1, 10);
        List<Activity> activities = Arrays.asList(
                new Activity("123 Main St", "Paris", LocalDateTime.of(2025, 1, 5, 10, 0), "sport", 50.0),
                new Activity("456 Elm St", "Paris", LocalDateTime.of(2025, 1, 6, 10, 0), "musique", 75.0),
                new Activity("789 Oak St", "Paris", LocalDateTime.of(2025, 1, 7, 10, 0), "art", 60.0)
        );
        when(activityRepository.findAll()).thenReturn(activities);

        // Act
        List<Activity> result = activityService.findActivities(criteria, "Paris", startDate, endDate);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(a -> a.getCategory().equals("sport")));
        assertTrue(result.stream().anyMatch(a -> a.getCategory().equals("musique")));
    }

    @Test
    void findActivities_shouldReturnEmptyListWhenNoCriteriaMatch() {
        // Arrange
        ActivityCriteria criteria = new ActivityCriteria();
        criteria.setCategories(Collections.singletonList("cinema"));
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 1, 10);
        List<Activity> activities = Arrays.asList(
                new Activity("123 Main St", "Paris", LocalDateTime.of(2025, 1, 5, 10, 0), "sport", 50.0),
                new Activity("456 Elm St", "Paris", LocalDateTime.of(2025, 1, 6, 10, 0), "musique", 75.0)
        );
        when(activityRepository.findAll()).thenReturn(activities);

        // Act
        List<Activity> result = activityService.findActivities(criteria, "Paris", startDate, endDate);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void userCannotAttendTwoActivitiesOnSameDate() {
        // Arrange
        ActivityCriteria criteria = new ActivityCriteria();
        LocalDate startDate = LocalDate.of(2025, 1, 15);
        LocalDate endDate = LocalDate.of(2025, 1, 20);
        List<Activity> activities = Arrays.asList(
                new Activity("123 Main St", "Paris", LocalDateTime.of(2025, 1, 16, 10, 0), "sport", 50.0),
                new Activity("456 Elm St", "Paris", LocalDateTime.of(2025, 1, 16, 14, 0), "musique", 75.0),
                new Activity("789 Oak St", "Paris", LocalDateTime.of(2025, 1, 17, 11, 0), "art", 60.0)
        );
        when(activityRepository.findAll()).thenReturn(activities);

        // Act
        List<Activity> result = activityService.findActivities(criteria, "Paris", startDate, endDate);

        // Assert
        assertEquals(2, result.size());
        assertNotEquals(result.get(0).getDateTime().toLocalDate(), result.get(1).getDateTime().toLocalDate());
    }

}
