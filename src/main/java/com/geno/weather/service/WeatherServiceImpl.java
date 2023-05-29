package com.geno.weather.service;

import com.geno.weather.data.Geolocation;
import com.geno.weather.data.IPAddressRequest;
import com.geno.weather.data.Weather;
import com.geno.weather.data.mapper.*;
import com.geno.weather.error.InvalidCoordinatesException;
import com.geno.weather.error.InvalidIPAddressException;
import com.geno.weather.repository.GeolocationRepository;
import com.geno.weather.repository.IPAddressRequestRepository;
import com.geno.weather.repository.WeatherRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {
    @Value("${IP_GEOLOCATION_API_URL}")
    private String ipGeolocationAPIURL;
    @Value("${CURRENT_WEATHER_API_URL}")
    private String weatherAPIURL;

    private final IPAddressRequestRepository ipAddressRequestRepository;
    private final GeolocationRepository geolocationRepository;
    private final WeatherRepository weatherRepository;
    private final RestTemplate restTemplate;
    private final JdbcTemplate jdbcTemplate;

    @Override
    @CircuitBreaker(name = "WeatherCircuitBreaker", fallbackMethod = "getWeatherFallBack")
    public WeatherDto getWeather(HttpServletRequest request) {
        String ipAddress = (request.getHeader("X-FORWARDED-FOR") == null)
                ? request.getRemoteAddr()
                : request.getHeader("X-FORWARDED-FOR");

        IPAddressRequest ipAddressEntity = saveRequestLog(ipAddress);//1 request logs

        GeolocationDto geolocation = getGeolocation(ipAddress);
        saveGeolocation(geolocation, ipAddressEntity); //2

        WeatherDto weather = getWeatherResponse(geolocation.getLatitude(), geolocation.getLongitude());
        saveWeatherResponseLogs(weather, ipAddressEntity); //3

        return weather;
    }

    public WeatherDto getWeatherFallBack(HttpServletRequest request, Exception ex) {
        return new WeatherDto();
    }

    private GeolocationDto getGeolocation(String ipAddress) {
        ResponseEntity<GeolocationDto> ipDataEntity = restTemplate.exchange(
                ipGeolocationAPIURL+ipAddress,
                HttpMethod.GET,
                null,
                GeolocationDto.class);

        return ipDataEntity.getBody();
    }

    private WeatherDto getWeatherResponse(String latitude, String longitude) throws RestClientException  {
        ResponseEntity<WeatherDto> weatherRestEntity = restTemplate.exchange(
                weatherAPIURL + latitude+","+longitude,
                HttpMethod.GET,
                null,
                WeatherDto.class);

        return weatherRestEntity.getBody();
    }

    private IPAddressRequest saveRequestLog(String ipAddress) {
        IPAddressRequest ipAddressRequest = new IPAddressRequest();
        ipAddressRequest.setIpAddress(ipAddress);
        ipAddressRequest.setCreatedOn(LocalDateTime.now());
        return ipAddressRequestRepository.save(ipAddressRequest);
    }

    private void saveGeolocation(GeolocationDto dto, IPAddressRequest ipAddress) {
        Geolocation geolocation = GeolocationMapper.GEOLOCATION_MAPPER.geolocationDtoToGeolocation(dto);
        geolocation.setCreatedOn(LocalDateTime.now());
        geolocation.setIpAddress(ipAddress);
        geolocationRepository.save(geolocation);
    }

    private void saveWeatherResponseLogs(WeatherDto dto, IPAddressRequest ipAddress) {
        Weather weather = WeatherMapper.WEATHER_MAPPER.weatherDtoToWeather(dto);
        weather.setIpAddress(ipAddress);
        weather.setCreatedOn(LocalDateTime.now());
        weatherRepository.save(weather);
    }

    @Override
    @Cacheable(value = "weather-analysis")
    public List<?> getWeatherAnalysis(final String latitude, final String longitude) {
        validateCoordinates(latitude, longitude);

        return jdbcTemplate.query(
                "select ip.ip_address, ip.created_on, g.country, g.city, " +
                        "w.temperature_celsius, w.\"condition\", w.humidity, w.wind_kph , w.wind_mph " +
                        "from geolocations g " +
                        "join weather_conditions w on g.ip_request_id = w.ip_request_id  " +
                        "join ip_requests ip on ip.id = g.ip_request_id  " +
                        "where g.latitude = ? and g.longitude = ?",
                ps -> {
                    ps.setString(1, latitude);
                    ps.setString(2, longitude);
                },
                new WeatherAnalysisMapper());
    }

    @Override
    @Cacheable(value = "weather-analysis")
    public List<?> getWeatherAnalysis(String ipAddress) {
        validateIpAddress(ipAddress);
        return jdbcTemplate.query(
                "select ip.ip_address, ip.created_on, g.country, g.city, " +
                        "w.temperature_celsius, w.\"condition\", w.humidity, w.wind_kph , w.wind_mph " +
                        "from geolocations g " +
                        "join weather_conditions w on g.ip_request_id = w.ip_request_id  " +
                        "join ip_requests ip on ip.id = g.ip_request_id  " +
                        "where ip.ip_address = ?",
                ps -> ps.setString(1, ipAddress),
                new WeatherAnalysisMapper());
    }

    private void validateCoordinates(String latitude, String longitude) {
        String latitudeRegex  = "^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?)$";
        String longitudeRegex = "^[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$";
        if (!latitude.matches(latitudeRegex) || !longitude.matches(longitudeRegex)) {
            throw new InvalidCoordinatesException();
        }
    }

    private void validateIpAddress(String ip) {
        String validIPRegEx = "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$";
        String reservedIPRegEx = "^(0\\.0\\.0\\.0|127|10|192\\.168|192\\.88\\.99).*$";
        if (!ip.matches(validIPRegEx) || (ip.matches(validIPRegEx) && ip.matches(reservedIPRegEx))) {
            throw new InvalidIPAddressException();
        }
    }
}
