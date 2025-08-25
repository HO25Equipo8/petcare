package com.petcare.back.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class GeocodingService {

    private final RestTemplate restTemplate = new RestTemplate();

    public double[] getCoordinatesFromAddress(String address) {
        String url = "https://nominatim.openstreetmap.org/search?format=json&q=" + address;

        ResponseEntity<List<Map<String, Object>>> response =
                restTemplate.exchange(url, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Map<String, Object>>>() {});

        if (response.getBody() != null && !response.getBody().isEmpty()) {
            Map<String, Object> location = response.getBody().get(0);
            double lat = Double.parseDouble((String) location.get("lat"));
            double lon = Double.parseDouble((String) location.get("lon"));
            return new double[]{lat, lon};
        }

        throw new RuntimeException("No se encontraron coordenadas para la dirección: " + address);
    }
}
