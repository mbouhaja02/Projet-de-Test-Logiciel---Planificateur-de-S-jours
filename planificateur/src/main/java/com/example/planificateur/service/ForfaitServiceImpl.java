package com.example.planificateur.service;

import com.example.planificateur.criteria.ForfaitCriteria;
import com.example.planificateur.domain.*;
import com.example.planificateur.service.model.Coordinates;
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
        LocalDateTime allerMax = criteria.getStartDate().atTime(21, 0); // par ex
        List<Transport> transportsAller = transportService.findTransports(
            criteria.getTransportCriteria(),
            criteria.getCityFrom(),
            criteria.getCityTo(),
            allerMin,
            allerMax
        );
        System.out.println("Transports aller trouvés : " + transportsAller.size());

        LocalDateTime retourDate = criteria.getStartDate().plusDays(criteria.getDurationInDays()).atTime(0, 0);
        LocalDateTime retourMax = retourDate.plusHours(21);
        List<Transport> transportsRetour = transportService.findTransports(
            criteria.getTransportCriteria(),
            criteria.getCityTo(),
            criteria.getCityFrom(),
            retourDate,
            retourMax
        );
        System.out.println("Transports retour trouvés : " + transportsRetour.size());

        // 2. Hotels
        List<Hotel> hotels = hotelService.findHotels(
            criteria.getHotelCriteria(),
            criteria.getCityTo(),
            criteria.getStartDate(),
            criteria.getStartDate().plusDays(criteria.getDurationInDays())
        );
        System.out.println("Hôtels trouvés : " + hotels.size());

        // 3. Activities
        List<Activity> allActivities = activityService.findActivities(
            criteria.getActivityCriteria(),
            criteria.getCityTo(),
            criteria.getStartDate(),
            criteria.getStartDate().plusDays(criteria.getDurationInDays())
        );
        System.out.println("Activités trouvées : " + allActivities.size() + "\n");

        // 4. On combine
        for (Transport aller : transportsAller) {
            for (Transport retour : transportsRetour) {
                // Ensure both transports are of the same mode
                /*if (aller.getMode() != retour.getMode()) {
                    continue; // Skip this combination if modes are different
                }*/
                for (Hotel hotel : hotels) {

                    // On va filtrer les activités en fonction de la distance max
                    List<Activity> filteredActivities = new ArrayList<>();
                    for (Activity act : allActivities) {
                        double distance = 0.0;
                        if (criteria.getActivityCriteria() != null && criteria.getActivityCriteria().getMaxDistance() != null) {
                            try {
                                Coordinates hotelCoords =
                                    geocodingService.geocodeAddress(hotel.getAddress());
                                Coordinates actCoords =
                                    geocodingService.geocodeAddress(act.getAddress());
                                distance = distanceService.computeDistance(hotelCoords, actCoords);
                            } catch (GeocodingException e) {
                                System.err.println("Geocoding error: " + e.getMessage());
                                continue;
                            }
                        }
                        if (criteria.getActivityCriteria() == null
                                || criteria.getActivityCriteria().getMaxDistance() == null
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
        //result.sort((f1, f2) -> Integer.compare(f2.getActivities().size(), f1.getActivities().size()));
        return result;
    }
}
