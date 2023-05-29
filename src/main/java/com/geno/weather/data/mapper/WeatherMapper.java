package com.geno.weather.data.mapper;

import com.geno.weather.data.Weather;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WeatherMapper {
    WeatherMapper WEATHER_MAPPER = Mappers.getMapper(WeatherMapper.class);

    @Mapping(source = "conditionText", target = "condition")
    Weather weatherDtoToWeather(WeatherDto dto);
}
