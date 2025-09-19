package com.petcare.back.service;

import com.petcare.back.domain.dto.request.LocationDTO;
import com.petcare.back.domain.entity.Location;
import com.petcare.back.domain.mapper.request.LocationCreateMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.LocationRepository;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final GeocodingService geocodingService;
    private final LocationCreateMapper locationMapper;


    public LocationService(LocationRepository locationRepository, GeocodingService geocodingService, LocationCreateMapper locationMapper) {
        this.locationRepository = locationRepository;
        this.geocodingService = geocodingService;
        this.locationMapper = locationMapper;
    }

    public Location save(LocationDTO dto, String placeId) {
        Location location = locationMapper.toEntity(dto);
        return save(location, placeId);
    }

    public Location save(Location location, String placeId) {
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
        location.setNumber(number); // reafirma el número por si fue modificado
        location.setPlaceId(placeId);

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

    public Location update(LocationDTO dto, Location existing) throws MyException {
        if (existing == null) {
            throw new MyException("No hay ubicación previa para actualizar.");
        }

        // Actualizamos los campos básicos
        existing.setStreet(dto.street());
        existing.setNumber(dto.number());
        existing.setCity(dto.city());
        existing.setProvince(dto.province());
        existing.setCountry(dto.country());
        existing.setPlaceId(dto.placeId());

        // Recalculamos coordenadas
        String fullAddress = buildAddress(dto.street() + " " + dto.number(), existing);
        double[] coords = geocodingService.getCoordinatesFromAddress(fullAddress);

        if (isInvalid(coords) && dto.street().contains(" ")) {
            String reducedStreet = getLastWord(dto.street());
            fullAddress = buildAddress(reducedStreet + " " + dto.number(), existing);
            coords = geocodingService.getCoordinatesFromAddress(fullAddress);
        }

        if (isInvalid(coords)) {
            throw new MyException("No se encontraron coordenadas para la dirección: " + fullAddress);
        }

        existing.setLatitude(coords[0]);
        existing.setLongitude(coords[1]);

        return locationRepository.save(existing);
    }
}
