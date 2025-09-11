package com.petcare.back.service;

import com.petcare.back.domain.dto.request.AutocompleteSuggestion;
import com.petcare.back.domain.dto.request.LocationDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

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

    public List<AutocompleteSuggestion> getAutocompleteSuggestions(String input) {
        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" +
                URLEncoder.encode(input, StandardCharsets.UTF_8) +
                "&types=address&key=" + apiKey;

        ResponseEntity<Map<String, Object>> response =
                restTemplate.exchange(url, HttpMethod.GET, null,
                        new ParameterizedTypeReference<>() {});

        Map<String, Object> body = response.getBody();
        if (body == null || !"OK".equals(body.get("status"))) {
            throw new RuntimeException("No se encontraron sugerencias para: " + input);
        }

        List<Map<String, Object>> predictions = (List<Map<String, Object>>) body.get("predictions");

        return predictions.stream()
                .map(p -> new AutocompleteSuggestion(
                        (String) p.get("description"),
                        (String) p.get("place_id")
                ))
                .toList();
    }

    public LocationDTO getLocationFromPlaceId(String placeId) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?place_id=" +
                placeId + "&key=" + apiKey;

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response == null || !"OK".equals(response.get("status"))) {
            throw new RuntimeException("No se encontró dirección para el placeId: " + placeId);
        }

        List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
        if (results.isEmpty()) {
            throw new RuntimeException("No se encontraron resultados");
        }

        Map<String, Object> firstResult = results.get(0);
        List<Map<String, Object>> addressComponents =
                (List<Map<String, Object>>) firstResult.get("address_components");

        String street = null, number = null, city = null, province = null, country = null;

        for (Map<String, Object> comp : addressComponents) {
            List<String> types = (List<String>) comp.get("types");
            String value = (String) comp.get("long_name");

            if (types.contains("route")) street = value;
            if (types.contains("street_number")) number = value;
            if (types.contains("locality")) city = value;
            if (types.contains("administrative_area_level_1")) province = value;
            if (types.contains("country")) country = value;
        }

        // Extraer lat/lng
        Map<String, Object> geometry = (Map<String, Object>) firstResult.get("geometry");
        Map<String, Object> location = (Map<String, Object>) geometry.get("location");

        Double lat = ((Number) location.get("lat")).doubleValue();
        Double lng = ((Number) location.get("lng")).doubleValue();

        // Podrías ampliar tu DTO para incluir lat/lng
        return new LocationDTO(street, number, city, province, country, lat, lng);
    }
    public AutocompleteSuggestion getPlaceIdFromLocationDTO(LocationDTO dto) {
        // Construimos la dirección en texto
        StringBuilder address = new StringBuilder();
        if (dto.street() != null) address.append(dto.street()).append(" ");
        if (dto.number() != null) address.append(dto.number()).append(", ");
        if (dto.city() != null) address.append(dto.city()).append(", ");
        if (dto.province() != null) address.append(dto.province()).append(", ");
        if (dto.country() != null) address.append(dto.country());

        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" +
                UriUtils.encode(address.toString(), StandardCharsets.UTF_8) +
                "&key=" + apiKey;

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response == null || !"OK".equals(response.get("status"))) {
            throw new RuntimeException("No se encontró placeId para la dirección: " + address);
        }

        List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
        if (results.isEmpty()) {
            throw new RuntimeException("No se encontraron resultados para la dirección: " + address);
        }

        Map<String, Object> firstResult = results.get(0);

        String description = (String) firstResult.get("formatted_address");
        String placeId = (String) firstResult.get("place_id");

        return new AutocompleteSuggestion(description, placeId);
    }
}
