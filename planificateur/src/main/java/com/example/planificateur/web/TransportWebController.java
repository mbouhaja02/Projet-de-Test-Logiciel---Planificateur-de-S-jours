package com.example.planificateur.web;

import com.example.planificateur.criteria.TransportCriteria;
import com.example.planificateur.domain.ModeTransport;
import com.example.planificateur.domain.Transport;
import com.example.planificateur.service.TransportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Contrôleur Thymeleaf : on rend des vues HTML dans /templates
 */
@Controller
public class TransportWebController {

    private final TransportService transportService;

    public TransportWebController(TransportService transportService) {
        this.transportService = transportService;
    }

    /**
     * Méthode GET : on affiche la page "search.html" sans résultats (formulaire vide).
     */
    @GetMapping("/search")
    public String showSearchPage() {
        return "search"; // Correspond à src/main/resources/templates/search.html
    }

    /**
     * Méthode POST : on traite le formulaire soumis, on fait la recherche,
     * puis on renvoie la même page "search.html" avec la liste "results".
     */
    @PostMapping("/search")
    public String processSearchForm(
        @RequestParam String cityFrom,
        @RequestParam String cityTo,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureMin,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureMax,
        @RequestParam(required = false) String mode,   // TRAIN, AVION, ou vide
        @RequestParam(required = false) String sort,   // cheapest, shortest, ou vide
        Model model
    ) {
        // Construire les critères
        TransportCriteria criteria = new TransportCriteria();
        if (mode != null && !mode.isEmpty()) {
            criteria.setPreferredMode(ModeTransport.valueOf(mode.toUpperCase()));
        }
        if ("cheapest".equalsIgnoreCase(sort)) {
            criteria.setPrioritizeCheapest(true);
        } else if ("shortest".equalsIgnoreCase(sort)) {
            criteria.setPrioritizeShortest(true);
        }

        // Appeler le service
        List<Transport> results = transportService.findTransports(
            criteria,
            cityFrom,
            cityTo,
            departureMin,
            departureMax
        );

        // Mettre la liste dans le Model => on pourra la boucler dans search.html
        model.addAttribute("results", results);

        return "search";
    }
}
