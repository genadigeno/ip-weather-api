package com.geno.weather.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ip_requests")
@Getter
@Setter
public class IPAddressRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String ipAddress;
    private LocalDateTime createdOn;

    @OneToOne(mappedBy = "ipAddress")
    @JsonBackReference
    private Weather weather;

    @OneToOne(mappedBy = "ipAddress")
    @JsonBackReference
    private Geolocation geolocation;
}
