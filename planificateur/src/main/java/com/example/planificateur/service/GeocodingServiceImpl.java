package com.example.planificateur.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.stereotype.Service;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Stub qui renvoie des coordonnées fictives. 
 * Dans la vraie vie, on appellerait un service de geocoding (ex: geocode.maps).
 */
@Service
/*public class GeocodingServiceStub implements GeocodingService {
    @Override
    public Coordinates geocodeAddress(String address) throws GeocodingException {
        // On renvoie un point fictif en fonction du hash du string, juste pour la démo
        int hash = Math.abs(address.hashCode());
        double lat = (hash % 90) - 45;      // un lat "bidon"
        double lon = (hash % 180) - 90;     // un lon "bidon"
        return new Coordinates(lat, lon);
    }
}*/
public class GeocodingServiceImpl implements GeocodingService {

    private static final String API_URL = "https://geocode.maps.co/search?q=";

    @Override
    public Coordinates geocodeAddress(String address) throws GeocodingException {
        if (address == null || address.trim().isEmpty()) {
            throw new GeocodingException("Address cannot be null or empty");
        }

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
            HttpGet request = new HttpGet(API_URL + encodedAddress);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    throw new GeocodingException("API returned status code: " + statusCode);
                }

                HttpEntity entity = response.getEntity();
                if (entity == null) {
                    throw new GeocodingException("Empty response from geocoding service");
                }

                String result = EntityUtils.toString(entity);
                JSONArray jsonArray = new JSONArray(result);

                if (jsonArray.length() == 0) {
                    throw new GeocodingException("No results found for address: " + address);
                }

                JSONObject firstResult = jsonArray.getJSONObject(0);
                double lat = firstResult.getDouble("lat");
                double lon = firstResult.getDouble("lon");

                return new Coordinates(lat, lon);
            }
        } catch (IOException e) {
            throw new GeocodingException("Error while geocoding address: " + address);
        } catch (JSONException e) {
            throw new GeocodingException("Error parsing geocoding response for address: " + address);
        }
    }
}
