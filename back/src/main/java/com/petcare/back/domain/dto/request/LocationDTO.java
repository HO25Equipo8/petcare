package com.petcare.back.domain.dto.request;

public record LocationDTO(
        String street,
        String number,
        String city,
        String province,
        String country,
        Double lat,
        Double lgn
) {}

