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
        // Concatenar dirección
        String fullAddress = String.format("%s %s, %s, %s, %s",
                location.getStreet(),
                location.getNumber(),
                location.getCity(),
                location.getProvince(),
                location.getCountry()
        );

        // Obtener coordenadas
        double[] coords = geocodingService.getCoordinatesFromAddress(fullAddress);
        location.setLatitude(coords[0]);
        location.setLongitude(coords[1]);

        return locationRepository.save(location);
    }
}
