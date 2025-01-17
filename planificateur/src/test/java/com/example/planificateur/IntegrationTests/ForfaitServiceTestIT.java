package com.example.planificateur.IntegrationTests;

import com.example.planificateur.criteria.ActivityCriteria;
import com.example.planificateur.criteria.HotelCriteria;
import com.example.planificateur.criteria.TransportCriteria;
import com.example.planificateur.domain.Activity;
import com.example.planificateur.domain.ModeTransport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.planificateur.criteria.ForfaitCriteria;
import com.example.planificateur.domain.Forfait;
import com.example.planificateur.service.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ForfaitServiceTestIT {

    @Autowired
    private ForfaitService forfaitService;

    @Test
    public void testBasicForfaitCreation() {
        ForfaitCriteria criteria = new ForfaitCriteria();
        criteria.setCityFrom("Paris");
        criteria.setCityTo("Lyon");
        criteria.setStartDate(LocalDate.of(2025, 1, 15));
        criteria.setDurationInDays(3);
        criteria.setMaxBudget(1000.0);

        criteria.setHotelCriteria(new HotelCriteria());
        criteria.setTransportCriteria(new TransportCriteria());
        criteria.setActivityCriteria(new ActivityCriteria());

        List<Forfait> forfaits = forfaitService.createForfaits(criteria);

        assertFalse(forfaits.isEmpty());
        for (Forfait forfait : forfaits) {
            assertNotNull(forfait.getAller());
            assertNotNull(forfait.getRetour());
            assertNotNull(forfait.getHotel());
            assertTrue(forfait.getTotalPrice() <= criteria.getMaxBudget());
        }
    }

    @Test
    public void testTransportCriteria() {
        ForfaitCriteria criteria = new ForfaitCriteria();
        criteria.setCityFrom("Paris");
        criteria.setCityTo("Lyon");
        criteria.setStartDate(LocalDate.of(2025, 1, 15));
        criteria.setDurationInDays(3);
        criteria.setMaxBudget(1000.0);

        TransportCriteria transportCriteria = new TransportCriteria();
        transportCriteria.setPreferredMode(ModeTransport.TRAIN);
        criteria.setTransportCriteria(transportCriteria);

        //other criterias
        criteria.setHotelCriteria(new HotelCriteria());
        criteria.setActivityCriteria(new ActivityCriteria());

        List<Forfait> forfaits = forfaitService.createForfaits(criteria);

        assertFalse(forfaits.isEmpty());
        for (Forfait forfait : forfaits) {
            assertEquals(ModeTransport.TRAIN, forfait.getAller().getMode());
            assertEquals(ModeTransport.TRAIN, forfait.getRetour().getMode());
        }
    }

    @Test
    public void testHotelCriteria() {
        ForfaitCriteria criteria = new ForfaitCriteria();
        criteria.setCityFrom("Paris");
        criteria.setCityTo("Lyon");
        criteria.setStartDate(LocalDate.of(2025, 1, 15));
        criteria.setDurationInDays(3);
        criteria.setMaxBudget(1000.0);

        HotelCriteria hotelCriteria = new HotelCriteria();
        hotelCriteria.setMinStars(3);
        criteria.setHotelCriteria(hotelCriteria);

        //other criteria
        criteria.setActivityCriteria(new ActivityCriteria());
        criteria.setTransportCriteria(new TransportCriteria());

        List<Forfait> forfaits = forfaitService.createForfaits(criteria);

        assertFalse(forfaits.isEmpty());
        for (Forfait forfait : forfaits) {
            assertTrue(forfait.getHotel().getStars() >= 3);
        }
    }

    @Test
    public void testActivityCriteria() {
        ForfaitCriteria criteria = new ForfaitCriteria();
        criteria.setCityFrom("Paris");
        criteria.setCityTo("Lyon");
        criteria.setStartDate(LocalDate.of(2025, 1, 15));
        criteria.setDurationInDays(3);
        criteria.setMaxBudget(1000.0);

        ActivityCriteria activityCriteria = new ActivityCriteria();
        activityCriteria.setMaxDistance(10.0);
        activityCriteria.setCategories(Arrays.asList("sport", "exposition"));
        criteria.setActivityCriteria(activityCriteria);

        //other criteria
        criteria.setTransportCriteria(new TransportCriteria());
        criteria.setHotelCriteria(new HotelCriteria());

        List<Forfait> forfaits = forfaitService.createForfaits(criteria);

        assertFalse(forfaits.isEmpty());
        for (Forfait forfait : forfaits) {
            assertFalse(forfait.getActivities().isEmpty());
            for (Activity activity : forfait.getActivities()) {
                assertTrue(activityCriteria.getCategories().contains(activity.getCategory()));
            }
        }
    }

    @Test
    public void testCreateForfaitIntegration() {
        ForfaitCriteria criteria = new ForfaitCriteria();
        criteria.setCityFrom("Paris");
        criteria.setCityTo("Lyon");
        criteria.setStartDate(LocalDate.of(2025, 1, 15));
        criteria.setDurationInDays(3);
        criteria.setMaxBudget(1000.0);

        // Initialiser TransportCriteria
        TransportCriteria transportCriteria = new TransportCriteria();
        transportCriteria.setPreferredMode(ModeTransport.TRAIN);
        transportCriteria.setPrioritizeCheapest(true);
        criteria.setTransportCriteria(transportCriteria);

        // Initialiser HotelCriteria
        HotelCriteria hotelCriteria = new HotelCriteria();
        hotelCriteria.setMinStars(3);
        criteria.setHotelCriteria(hotelCriteria);

        // Initialiser ActivityCriteria
        ActivityCriteria activityCriteria = new ActivityCriteria();
        activityCriteria.setMaxDistance(10.0);
        activityCriteria.setCategories(Arrays.asList("sport", "exposition"));
        criteria.setActivityCriteria(activityCriteria);

        List<Forfait> forfaits = forfaitService.createForfaits(criteria);

        assertFalse(forfaits.isEmpty());
        for (Forfait forfait : forfaits) {
            assertNotNull(forfait.getAller());
            assertNotNull(forfait.getRetour());
            assertNotNull(forfait.getHotel());
            assertFalse(forfait.getActivities().isEmpty());
            assertTrue(forfait.getTotalPrice() <= criteria.getMaxBudget());
            assertEquals(ModeTransport.TRAIN, forfait.getAller().getMode());
            assertEquals(ModeTransport.TRAIN, forfait.getRetour().getMode());
            assertTrue(forfait.getHotel().getStars() >= 3);
            for (Activity activity : forfait.getActivities()) {
                assertTrue(activityCriteria.getCategories().contains(activity.getCategory()));
            }
        }
    }

    @Test
    public void testNoForfaitsFound() {
        ForfaitCriteria criteria = new ForfaitCriteria();
        criteria.setCityFrom("NonExistentCity");
        criteria.setCityTo("Lyon");
        criteria.setStartDate(LocalDate.of(2025, 1, 15));
        criteria.setDurationInDays(3);
        criteria.setMaxBudget(1000.0);

        criteria.setHotelCriteria(new HotelCriteria());
        criteria.setTransportCriteria(new TransportCriteria());
        criteria.setActivityCriteria(new ActivityCriteria());

        List<Forfait> forfaits = forfaitService.createForfaits(criteria);

        assertTrue(forfaits.isEmpty(), "No forfaits should be found for non-existent city.");
    }

}







