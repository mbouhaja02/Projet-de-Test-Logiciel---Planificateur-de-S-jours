package com.example.planificateur.service;

import com.example.planificateur.service.model.Coordinates;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Service
public class GeocodingServiceImpl implements GeocodingService {
    private static final String BASE_URL = "https://geocode.maps.co/search";
    protected final ObjectMapper objectMapper;

    @Value("${geocoding.api.key}")
    private String apiKey;

    public GeocodingServiceImpl() {
        this.objectMapper = new ObjectMapper();
    }

    public URL createUrl(String address) throws Exception {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("q", address)
                .queryParam("api_key", apiKey)
                .build()
                .encode()
                .toUriString();
        return new URL(url);
    }

    public HttpURLConnection createConnection(URL url) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.setRequestProperty("Accept", "application/json");
        return conn;
    }

    public String readResponse(HttpURLConnection conn) throws Exception {
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }
        return response.toString();
    }

    @Override
    public Coordinates geocodeAddress(String address) throws GeocodingException {
        if (address == null || address.trim().isEmpty()) {
            throw new GeocodingException("Address cannot be null or empty");
        }

        try {
            URL apiUrl = createUrl(address);
            HttpURLConnection conn = createConnection(apiUrl);
            String jsonResponse = readResponse(conn);

            JsonNode jsonArray = objectMapper.readTree(jsonResponse);

            /*if (jsonArray.isEmpty()) {
                throw new GeocodingException("No results found for address: " + address);
            }*/

            JsonNode firstResult = jsonArray.get(0);
            double lat = Double.parseDouble(firstResult.get("lat").asText());
            double lon = Double.parseDouble(firstResult.get("lon").asText());

            return new Coordinates(lat, lon);

        } catch (Exception e) {
            throw new GeocodingException("Error while geocoding address: " + address + ". Error: " + e.getMessage(), e);
        }
    }
}