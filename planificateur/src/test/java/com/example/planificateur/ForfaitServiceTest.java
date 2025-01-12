package com.example.planificateur;

import com.example.planificateur.criteria.*;
import com.example.planificateur.domain.*;
import com.example.planificateur.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ForfaitServiceImplTest {

    @Mock private TransportService transportService;
    @Mock private HotelService hotelService;
    @Mock private ActivityService activityService;
    @Mock private GeocodingService geocodingService;
    @Mock private DistanceService distanceService;

    private ForfaitServiceImpl forfaitService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        forfaitService = new ForfaitServiceImpl(transportService, hotelService, activityService, geocodingService, distanceService);
    }

    @Test
    void createForfaits_shouldReturnValidForfaits() {
        // Arrange
        ForfaitCriteria criteria = new ForfaitCriteria();
        criteria.setCityFrom("Paris");
        criteria.setCityTo("Lyon");
        criteria.setStartDate(LocalDate.of(2025, 1, 15));
        criteria.setDurationInDays(3);
        criteria.setMaxBudget(1000);
        criteria.setActivityCriteria(new ActivityCriteria());

        Transport aller = new Transport("Paris", "Lyon", LocalDateTime.of(2025, 1, 15, 10, 0), LocalDateTime.of(2025, 1, 15, 12, 0), ModeTransport.TRAIN, 100);
        Transport retour = new Transport("Lyon", "Paris", LocalDateTime.of(2025, 1, 18, 14, 0), LocalDateTime.of(2025, 1, 18, 16, 0), ModeTransport.TRAIN, 100);
        Hotel hotel = new Hotel("123 Main St", "Lyon", 3, 100);
        Activity activity = new Activity("456 Elm St", "Lyon", LocalDateTime.of(2025, 1, 16, 10, 0), "museum", 50);

        when(transportService.findTransports(any(), eq("Paris"), eq("Lyon"), any(), any())).thenReturn(Arrays.asList(aller));
        when(transportService.findTransports(any(), eq("Lyon"), eq("Paris"), any(), any())).thenReturn(Arrays.asList(retour));
        when(hotelService.findHotels(any(), eq("Lyon"), any(), any())).thenReturn(Arrays.asList(hotel));
        when(activityService.findActivities(any(), eq("Lyon"), any(), any())).thenReturn(Arrays.asList(activity));

        // Act
        List<Forfait> result = forfaitService.createForfaits(criteria);

        // Assert
        assertEquals(1, result.size());
        Forfait forfait = result.get(0);
        assertEquals(aller, forfait.getAller());
        assertEquals(retour, forfait.getRetour());
        assertEquals(hotel, forfait.getHotel());
        assertEquals(1, forfait.getActivities().size());
        assertEquals(activity, forfait.getActivities().get(0));
        assertEquals(550, forfait.getTotalPrice()); // 100 + 100 + (100 * 3) + 50
    }

    @Test
    void createForfaits_shouldRespectMaxBudget() {
        // Arrange
        ForfaitCriteria criteria = new ForfaitCriteria();
        criteria.setCityFrom("Paris");
        criteria.setCityTo("Lyon");
        criteria.setStartDate(LocalDate.of(2025, 1, 15));
        criteria.setDurationInDays(3);
        criteria.setMaxBudget(500);
        criteria.setActivityCriteria(new ActivityCriteria());

        Transport aller = new Transport("Paris", "Lyon", LocalDateTime.of(2025, 1, 15, 10, 0), LocalDateTime.of(2025, 1, 15, 12, 0), ModeTransport.TRAIN, 100);
        Transport retour = new Transport("Lyon", "Paris", LocalDateTime.of(2025, 1, 18, 14, 0), LocalDateTime.of(2025, 1, 18, 16, 0), ModeTransport.TRAIN, 100);
        Hotel hotel = new Hotel("123 Main St", "Lyon", 3, 100);
        Activity activity = new Activity("456 Elm St", "Lyon", LocalDateTime.of(2025, 1, 16, 10, 0), "museum", 50);

        when(transportService.findTransports(any(), eq("Paris"), eq("Lyon"), any(), any())).thenReturn(Arrays.asList(aller));
        when(transportService.findTransports(any(), eq("Lyon"), eq("Paris"), any(), any())).thenReturn(Arrays.asList(retour));
        when(hotelService.findHotels(any(), eq("Lyon"), any(), any())).thenReturn(Arrays.asList(hotel));
        when(activityService.findActivities(any(), eq("Lyon"), any(), any())).thenReturn(Arrays.asList(activity));

        // Act
        List<Forfait> result = forfaitService.createForfaits(criteria);

        // Assert
        assertTrue(result.isEmpty());
    }
}
