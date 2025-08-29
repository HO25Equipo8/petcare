package com.petcare.back.service;

import com.petcare.back.domain.entity.Location;
import com.petcare.back.repository.LocationRepository;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final GeocodingService geocodingService;

    public LocationService(LocationRepository locationRepository, GeocodingService geocodingService) {
        this.locationRepository = locationRepository;
        this.geocodingService = geocodingService;
    }

    public Location save(Location location) {
        String number = location.getNumber();
        String street = location.getStreet();

        String fullAddress = buildAddress(street + " " + number, location);
        double[] coords = geocodingService.getCoordinatesFromAddress(fullAddress);

        if (isInvalid(coords) && street.contains(" ")) {
            String reducedStreet = getLastWord(street);
            fullAddress = buildAddress(reducedStreet + " " + number, location);
            coords = geocodingService.getCoordinatesFromAddress(fullAddress);
        }

        if (isInvalid(coords)) {
            throw new RuntimeException("No se encontraron coordenadas para la dirección: " + fullAddress);
        }

        location.setLatitude(coords[0]);
        location.setLongitude(coords[1]);
        location.setNumber(number);

        return locationRepository.save(location);
    }

    private boolean isInvalid(double[] coords) {
        return coords == null || coords.length != 2 || coords[0] == 0.0 || coords[1] == 0.0;
    }

    private String getLastWord(String text) {
        String[] parts = text.trim().split(" ");
        return parts[parts.length - 1];
    }

    private String buildAddress(String calle, Location loc) {
        return String.join(", ",
                calle,
                loc.getCity(),
                loc.getProvince(),
                loc.getCountry()
        );
    }
}
