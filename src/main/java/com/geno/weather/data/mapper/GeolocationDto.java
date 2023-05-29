package com.geno.weather.data.mapper;

import lombok.Data;

@Data
public class GeolocationDto {
    private String country;
    private String city;
    private String latitude;
    private String longitude;
}
