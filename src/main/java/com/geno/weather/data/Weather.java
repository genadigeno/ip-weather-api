package com.geno.weather.data;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "weather_conditions")
@Getter
@Setter
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ip_request_id", referencedColumnName = "id")
    @JsonManagedReference
    private IPAddressRequest ipAddress;

    private double temperatureCelsius;
    private double temperatureFahrenheit;
    private double windKph;
    private double windMph;
    private String condition;
    private int conditionCode;
    private int humidity;
    private LocalDateTime createdOn;
}