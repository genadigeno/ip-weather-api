package com.geno.weather.service;

import com.geno.weather.data.Weather;
import com.geno.weather.data.mapper.WeatherDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface WeatherService {
    WeatherDto getWeather(HttpServletRequest request);
    List<?> getWeatherAnalysis(String latitude, String longitude);
    List<?> getWeatherAnalysis(String ipAddress);
}
