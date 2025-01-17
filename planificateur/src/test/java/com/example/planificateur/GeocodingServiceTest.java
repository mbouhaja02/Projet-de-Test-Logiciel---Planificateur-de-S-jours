package com.example.planificateur;

import com.example.planificateur.service.model.Coordinates;
import com.example.planificateur.service.GeocodingException;
import com.example.planificateur.service.GeocodingServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GeocodingServiceTest {

    @Spy
    @InjectMocks
    private GeocodingServiceImpl geocodingService;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void geocodeAddress_validAddress_returnsCoordinates() throws Exception {
        String address = "85 Quai Joseph Gillet, 69004 Lyon";
        URL mockUrl = mock(URL.class);
        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        String jsonResponse = "[{\"lat\":\"45.7885776\",\"lon\":\"4.8198274\"}]";

        doReturn(mockUrl).when(geocodingService).createUrl(address);
        doReturn(mockConnection).when(geocodingService).createConnection(mockUrl);
        doReturn(jsonResponse).when(geocodingService).readResponse(mockConnection);

        Coordinates result = geocodingService.geocodeAddress(address);

        assertEquals(45.7885776, result.getLatitude());
        assertEquals(4.8198274, result.getLongitude());
    }

    @Test
    public void geocodeAddress_emptyAddress_throwsException() {
        Exception exception = assertThrows(GeocodingException.class, () -> {
            geocodingService.geocodeAddress("");
        });

        assertEquals("Address cannot be null or empty", exception.getMessage());
    }

    @Test
    public void geocodeAddress_nullAddress_throwsException() {
        Exception exception = assertThrows(GeocodingException.class, () -> {
            geocodingService.geocodeAddress(null);
        });

        assertEquals("Address cannot be null or empty", exception.getMessage());
    }

    @Test
    void geocodeAddress_apiError_throwsException() throws Exception {
        String address = "85 Quai Joseph Gillet, 69004 Lyon";
        URL mockUrl = mock(URL.class);
        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        RuntimeException apiError = new RuntimeException("API error");

        doReturn(mockUrl).when(geocodingService).createUrl(address);
        doReturn(mockConnection).when(geocodingService).createConnection(mockUrl);
        doThrow(apiError).when(geocodingService).readResponse(mockConnection);

        GeocodingException exception = assertThrows(GeocodingException.class,
                () -> geocodingService.geocodeAddress(address));

        assertEquals("Error while geocoding address: " + address + ". Error: API error",
                exception.getMessage());
        assertEquals(apiError, exception.getCause());
    }

    @Test
    void createUrl_validAddress_returnsCorrectUrl() throws Exception {
        String address = "85 Quai Joseph Gillet, 69004 Lyon";
        String expectedUrl = "https://geocode.maps.co/search?q=85%20Quai%20Joseph%20Gillet,%2069004%20Lyon&api_key"; // Replace null with actual API key if needed

        URL url = geocodingService.createUrl(address);

        assertEquals(expectedUrl, url.toString(), "The created URL should match the expected URL.");
    }

    @Test
    void createConnection_validUrl_returnsHttpURLConnection() throws Exception {
        URL mockUrl = mock(URL.class);
        HttpURLConnection mockConnection = mock(HttpURLConnection.class);

        when(mockUrl.openConnection()).thenReturn(mockConnection);

        HttpURLConnection connection = geocodingService.createConnection(mockUrl);

        assertEquals(mockConnection, connection, "The connection should match the mocked connection.");

        verify(mockConnection).setRequestMethod("GET");
        verify(mockConnection).setRequestProperty("User-Agent", "Mozilla/5.0");
        verify(mockConnection).setRequestProperty("Accept", "application/json");
    }

    @Test
    void readResponse_validConnection_returnsResponseString() throws Exception {
        String jsonResponse = "[{\"lat\": \"45.7885776\", \"lon\": \"4.8198274\"}]";  // Correct format
        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        InputStream mockInputStream = new ByteArrayInputStream(jsonResponse.getBytes(StandardCharsets.UTF_8));

        when(mockConnection.getInputStream()).thenReturn(mockInputStream);

        String response = geocodingService.readResponse(mockConnection);

        assertEquals(jsonResponse, response, "The response should match the expected JSON string");
    }
}
