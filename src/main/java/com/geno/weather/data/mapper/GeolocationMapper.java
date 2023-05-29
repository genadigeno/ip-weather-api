package com.geno.weather.data.mapper;

import com.geno.weather.data.Geolocation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GeolocationMapper {
    GeolocationMapper GEOLOCATION_MAPPER = Mappers.getMapper(GeolocationMapper.class);

    Geolocation geolocationDtoToGeolocation(GeolocationDto dto);
}
