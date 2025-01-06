package com.example.planificateur.service;

import com.example.planificateur.criteria.ActivityCriteria;
import com.example.planificateur.criteria.ForfaitCriteria;
import com.example.planificateur.domain.*;
import com.example.planificateur.service.GeocodingException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ForfaitServiceImpl implements ForfaitService {

    private final TransportService transportService;
    private final HotelService hotelService;
    private final ActivityService activityService;
    private final GeocodingService geocodingService;
    private final DistanceService distanceService;

    public ForfaitServiceImpl(TransportService transportService,
                              HotelService hotelService,
                              ActivityService activityService,
                              GeocodingService geocodingService,
                              DistanceService distanceService) {
        this.transportService = transportService;
        this.hotelService = hotelService;
        this.activityService = activityService;
        this.geocodingService = geocodingService;
        this.distanceService = distanceService;
    }

    @Override
    public List<Forfait> createForfaits(ForfaitCriteria criteria) {
        List<Forfait> result = new ArrayList<>();

        // 1. Transports aller/retour
        LocalDateTime allerMin = criteria.getStartDate().atTime(0, 0);
        LocalDateTime allerMax = criteria.getStartDate().atTime(12, 0); // par ex
        List<Transport> transportsAller = transportService.findTransports(
            criteria.getTransportCriteria(),
            criteria.getCityFrom(),
            criteria.getCityTo(),
            allerMin,
            allerMax
        );

        LocalDateTime retourDate = criteria.getStartDate().plusDays(criteria.getDurationInDays()).atTime(0, 0);
        LocalDateTime retourMax = retourDate.plusHours(12);
        List<Transport> transportsRetour = transportService.findTransports(
            criteria.getTransportCriteria(),
            criteria.getCityTo(),
            criteria.getCityFrom(),
            retourDate,
            retourMax
        );

        // 2. Hotels
        List<Hotel> hotels = hotelService.findHotels(
            criteria.getHotelCriteria(),
            criteria.getCityTo(),
            criteria.getStartDate(),
            criteria.getStartDate().plusDays(criteria.getDurationInDays())
        );

        // 3. Activities
        List<Activity> allActivities = activityService.findActivities(
            criteria.getActivityCriteria(),
            criteria.getCityTo(),
            criteria.getStartDate(),
            criteria.getStartDate().plusDays(criteria.getDurationInDays())
        );

        // 4. On combine
        for (Transport aller : transportsAller) {
            for (Transport retour : transportsRetour) {
                for (Hotel hotel : hotels) {

                    // On va filtrer les activités en fonction de la distance max
                    List<Activity> filteredActivities = new ArrayList<>();
                    for (Activity act : allActivities) {
                        double distance = 0.0;
                        if (criteria.getActivityCriteria().getMaxDistance() != null) {
                            try {
                                GeocodingService.Coordinates hotelCoords =
                                    geocodingService.geocodeAddress(hotel.getAddress());
                                GeocodingService.Coordinates actCoords =
                                    geocodingService.geocodeAddress(act.getAddress());
                                distance = distanceService.computeDistance(hotelCoords, actCoords);
                            } catch (GeocodingException e) {
                                // On peut logger, ou ajouter une erreur
                            }
                        }
                        if (criteria.getActivityCriteria().getMaxDistance() == null
                            || distance <= criteria.getActivityCriteria().getMaxDistance()) {
                            filteredActivities.add(act);
                        }
                    }

                    // Construction du forfait
                    Forfait f = new Forfait();
                    f.setAller(aller);
                    f.setRetour(retour);
                    f.setHotel(hotel);
                    f.setActivities(filteredActivities);

                    double totalTransport = aller.getPrice() + retour.getPrice();
                    double totalHotel = hotel.getPricePerNight() * criteria.getDurationInDays();
                    double totalActivities = filteredActivities.stream().mapToDouble(Activity::getPrice).sum();
                    double total = totalTransport + totalHotel + totalActivities;
                    f.setTotalPrice(total);

                    // Vérifier budget
                    if (total <= criteria.getMaxBudget()) {
                        result.add(f);
                    }
                }
            }
        }

        return result;
    }
}
