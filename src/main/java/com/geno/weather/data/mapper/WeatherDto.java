package com.geno.weather.data.mapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class WeatherDto {
    private Double temperatureCelsius;
    private Double temperatureFahrenheit;
    private Double windKph;
    private Double windMph;
    private Integer humidity;
    private String conditionText;
    private Integer conditionCode;

    @JsonProperty("current")
    private void reorganizeCurrentWeather(Map<String, Object> current) {
        this.temperatureCelsius = (Double) current.get("temp_c");
        this.temperatureFahrenheit = (Double) current.get("temp_f");
        this.windMph  = (Double) current.get("wind_mph");
        this.windKph  = (Double) current.get("wind_kph");
        this.humidity = (Integer) current.get("humidity");

        Object conditionMap = current.get("condition");
        if (conditionMap instanceof Map<?, ?>) {
            Map<String, Object> condition = (Map<String, Object>) conditionMap;
            this.conditionText = (String) condition.get("text");
            this.conditionCode = (Integer) condition.get("code");
        }
    }
}
