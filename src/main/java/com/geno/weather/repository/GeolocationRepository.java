package com.geno.weather.repository;

import com.geno.weather.data.Geolocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeolocationRepository extends JpaRepository<Geolocation, Integer>, JpaSpecificationExecutor<Geolocation> {
    List<Geolocation> getGeolocationByLatitudeAndLongitude(String latitude, String longitude);
}
