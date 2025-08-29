package com.petcare.back.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class GeocodingService {

    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${api.key.google}")
    private String apiKey;

    public double[] getCoordinatesFromAddress(String address) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" +
                URLEncoder.encode(address, StandardCharsets.UTF_8) +
                "&key=" + apiKey;

        ResponseEntity<Map<String, Object>> response =
                restTemplate.exchange(url, HttpMethod.GET, null,
                        new ParameterizedTypeReference<>() {});

        Map<String, Object> body = response.getBody();
        if (body == null || !"OK".equals(body.get("status"))) {
            throw new RuntimeException("No se encontraron coordenadas para la dirección: " + address);
        }

        List<Map<String, Object>> results = (List<Map<String, Object>>) body.get("results");
        if (results.isEmpty()) {
            throw new RuntimeException("No se encontraron coordenadas para la dirección: " + address);
        }

        Map<String, Object> geometry = (Map<String, Object>) ((Map<String, Object>) results.get(0).get("geometry")).get("location");
        double lat = ((Number) geometry.get("lat")).doubleValue();
        double lng = ((Number) geometry.get("lng")).doubleValue();

        return new double[]{lat, lng};
    }
}
