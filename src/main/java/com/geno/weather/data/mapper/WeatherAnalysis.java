package com.geno.weather.data.mapper;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class WeatherAnalysis implements Serializable {
    private String ipAddress;
    private String country;
    private String city;
    private LocalDateTime date;
    private Float temperatureCelsius;
    private Float windKph;
    private Float windMph;
    private String condition;
    private Integer conditionCode;
    private Integer humidity;
}
