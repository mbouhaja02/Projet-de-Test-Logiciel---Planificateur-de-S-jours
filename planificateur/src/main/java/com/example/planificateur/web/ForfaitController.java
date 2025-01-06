package com.example.planificateur.web;

import com.example.planificateur.criteria.*;
import com.example.planificateur.service.ForfaitService;
import com.example.planificateur.domain.Forfait;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ForfaitController {

    private final ForfaitService forfaitService;

    public ForfaitController(ForfaitService forfaitService) {
        this.forfaitService = forfaitService;
    }

    @GetMapping("/forfait")
    public String showForm() {
        // Affiche la page "forfait-search.html"
        return "forfait-search";
    }

    @PostMapping("/forfait")
    public String searchForfaits(@RequestParam String cityFrom,
                                 @RequestParam String cityTo,
                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                 @RequestParam int durationInDays,
                                 @RequestParam double maxBudget,
                                 // transport
                                 @RequestParam(required = false) String mode,
                                 @RequestParam(required = false) String transportSort,
                                 // hotel
                                 @RequestParam(defaultValue = "0") int minStars,
                                 @RequestParam(required = false) String hotelSort,
                                 // activities
                                 @RequestParam(required = false) List<String> categories,
                                 @RequestParam(required = false) Double maxDistance,
                                 Model model) {

        // Construire ForfaitCriteria
        ForfaitCriteria fc = new ForfaitCriteria();
        fc.setCityFrom(cityFrom);
        fc.setCityTo(cityTo);
        fc.setStartDate(startDate);
        fc.setDurationInDays(durationInDays);
        fc.setMaxBudget(maxBudget);

        // Transport
        TransportCriteria tc = new TransportCriteria();
        if (mode != null && !mode.isEmpty()) {
            // TRAIN ou AVION
            tc.setPreferredMode(Enum.valueOf(com.example.planificateur.domain.ModeTransport.class, mode));
        }
        if ("cheapest".equalsIgnoreCase(transportSort)) {
            tc.setPrioritizeCheapest(true);
        } else if ("shortest".equalsIgnoreCase(transportSort)) {
            tc.setPrioritizeShortest(true);
        }
        fc.setTransportCriteria(tc);

        // Hotel
        HotelCriteria hc = new HotelCriteria();
        hc.setMinStars(minStars);
        if ("cheapest".equalsIgnoreCase(hotelSort)) {
            hc.setPrioritizeCheapest(true);
        } else if ("maxstars".equalsIgnoreCase(hotelSort)) {
            hc.setPrioritizeMaxStars(true);
        }
        fc.setHotelCriteria(hc);

        // Activities
        ActivityCriteria ac = new ActivityCriteria();
        if (categories != null) {
            // ex: ["sport","exposition"]
            // on peut les mettre en minuscules
            ac.setCategories(categories.stream().map(String::toLowerCase).toList());
        }
        ac.setMaxDistance(maxDistance);
        fc.setActivityCriteria(ac);

        // Appeler ForfaitService
        List<Forfait> results = forfaitService.createForfaits(fc);

        // Mettre la liste dans le Model
        model.addAttribute("results", results);

        // Renvoyer la même page (forfait-search.html) mais on affichera la liste
        return "forfait-search";
    }
}
