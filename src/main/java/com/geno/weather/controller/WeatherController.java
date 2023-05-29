package com.geno.weather.controller;

import com.geno.weather.service.WeatherService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    @CacheEvict(value = "weather-analysis", allEntries = true)
    public ResponseEntity<?> weather(HttpServletRequest request) {
        return ResponseEntity.ok(weatherService.getWeather(request));
    }

    @GetMapping("/coordinates")
    public ResponseEntity<List<?>> weatherAnalysisCoordinates(@RequestParam("latitude") String latitude,
                                                   @RequestParam("longitude") String longitude) {
        return ResponseEntity.ok(weatherService.getWeatherAnalysis(latitude, longitude));
    }

    @GetMapping("/ip")
    public ResponseEntity<List<?>> weatherAnalysisIpAddress(@RequestParam("ip") String ip) {
        return ResponseEntity.ok(weatherService.getWeatherAnalysis(ip));
    }
}
