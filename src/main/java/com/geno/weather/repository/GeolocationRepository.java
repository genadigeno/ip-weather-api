package com.geno.weather.repository;

import com.geno.weather.data.Geolocation;
import com.geno.weather.data.Weather;
import jakarta.persistence.criteria.Join;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeolocationRepository extends JpaRepository<Geolocation, Integer>, JpaSpecificationExecutor<Geolocation> {

    /*static Specification<Geolocation> hasBookWithTitle(String bookTitle) {
        return (root, query, criteriaBuilder) -> {
            Join<Weather, Geolocation> authorsBook = root.join("weather_conditions");
            return criteriaBuilder.equal(authorsBook.get("title"), bookTitle);
        };
    }*/

    List<Geolocation> getGeolocationByLatitudeAndLongitude(String latitude, String longitude);
}
