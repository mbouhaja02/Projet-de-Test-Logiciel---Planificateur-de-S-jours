package com.example.planificateur;

import com.example.planificateur.service.GeocodingException;
import com.example.planificateur.service.GeocodingServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GeocodingServiceImplTest {

    private GeocodingServiceImpl geocodingService;
    private MockedStatic<HttpClients> httpClientsMock;

    @Mock
    private CloseableHttpClient httpClient;
    @Mock
    private CloseableHttpResponse httpResponse;
    @Mock
    private HttpEntity httpEntity;
    @Mock
    private StatusLine statusLine;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        geocodingService = new GeocodingServiceImpl();
        httpClientsMock = mockStatic(HttpClients.class);
        httpClientsMock.when(HttpClients::createDefault).thenReturn(httpClient);
    }

    @AfterEach
    void tearDown() {
        if (httpClientsMock != null) {
            httpClientsMock.close();
        }
    }

    @Test
    void geocodeAddress_validAddress_returnsCoordinates() throws Exception {
        // Arrange
        String address = "1600 Amphitheatre Parkway, Mountain View, CA";
        String jsonResponse = "[{\"lat\":\"37.4224764\",\"lon\":\"-122.0842499\"}]";

        when(httpClient.execute(any())).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(httpResponse.getEntity()).thenReturn(httpEntity);
        when(httpEntity.getContent()).thenReturn(
                new ByteArrayInputStream(jsonResponse.getBytes(StandardCharsets.UTF_8)));

        // Act
        var result = geocodingService.geocodeAddress(address);

        // Assert
        assertNotNull(result);
        assertEquals(37.4224764, result.getLatitude(), 0.0000001);
        assertEquals(-122.0842499, result.getLongitude(), 0.0000001);
    }

    @Test
    void geocodeAddress_emptyAddress_throwsException() {
        assertThrows(GeocodingException.class, () ->
                geocodingService.geocodeAddress(""));
    }

    @Test
    void geocodeAddress_nullAddress_throwsException() {
        assertThrows(GeocodingException.class, () ->
                geocodingService.geocodeAddress(null));
    }

    @Test
    void geocodeAddress_apiError_throwsException() throws Exception {
        // Arrange
        String address = "Some Address";
        when(httpClient.execute(any())).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(500);

        // Act & Assert
        var exception = assertThrows(GeocodingException.class, () ->
                geocodingService.geocodeAddress(address));
        assertTrue(exception.getMessage().contains("500"));
    }

    @Test
    void geocodeAddress_noResults_throwsException() throws Exception {
        // Arrange
        String address = "Non-existent Address";
        String jsonResponse = "[]";

        when(httpClient.execute(any())).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(httpResponse.getEntity()).thenReturn(httpEntity);
        when(httpEntity.getContent()).thenReturn(
                new ByteArrayInputStream(jsonResponse.getBytes(StandardCharsets.UTF_8)));

        // Act & Assert
        var exception = assertThrows(GeocodingException.class, () ->
                geocodingService.geocodeAddress(address));
        assertTrue(exception.getMessage().contains("No results found"));
    }
}