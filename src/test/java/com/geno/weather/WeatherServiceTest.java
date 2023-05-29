package com.geno.weather;

import com.geno.weather.data.Geolocation;
import com.geno.weather.data.IPAddressRequest;
import com.geno.weather.data.Weather;
import com.geno.weather.data.mapper.GeolocationDto;
import com.geno.weather.data.mapper.WeatherDto;
import com.geno.weather.error.InvalidCoordinatesException;
import com.geno.weather.error.InvalidIPAddressException;
import com.geno.weather.repository.GeolocationRepository;
import com.geno.weather.repository.IPAddressRequestRepository;
import com.geno.weather.repository.WeatherRepository;
import com.geno.weather.service.WeatherServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class WeatherServiceTest {
    @InjectMocks
    private WeatherServiceImpl weatherService;

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @Mock
    private IPAddressRequestRepository ipAddressRequestRepository;
    @Mock
    private GeolocationRepository geolocationRepository;
    @Mock
    private WeatherRepository weatherRepository;

    private MockHttpServletRequest mockHttpServletRequest;
    private String ipGeolocationAPIURL;
    private String weatherAPIURL;
    private GeolocationDto geolocationDto;
    private WeatherDto weatherDto;

    @BeforeEach
    public void init(){
        ipGeolocationAPIURL = "https://api.ipgeolocation.io/ipgeo?apiKey=17b1853e872a490eb0b38f3a3ab42aac&ip=";
        weatherAPIURL = "https://api.weatherapi.com/v1/current.json?key=64aa1425a3fe4291aad113411232505&q=";

        mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setRemoteAddr("5.178.192.251");

        geolocationDto = new GeolocationDto();
        geolocationDto.setCity("Tbilisi");
        geolocationDto.setCountry("Georgia");
        geolocationDto.setLatitude("41.70907");
        geolocationDto.setLongitude("44.79610");

        weatherDto = new WeatherDto();

        ReflectionTestUtils.setField(weatherService, "ipGeolocationAPIURL", ipGeolocationAPIURL);
        ReflectionTestUtils.setField(weatherService, "weatherAPIURL", weatherAPIURL);
    }

    @Test
    public void getWeatherAnalysis_Coordinates_test(){
        when(jdbcTemplate.query(anyString(), any(PreparedStatementSetter.class), any(RowMapper.class)))
                .thenReturn(Collections.singletonList(List.class));

        assertDoesNotThrow(() -> {
            weatherService.getWeatherAnalysis("41.70907", "44.79610");
        });
    }

    @Test
    public void getWeatherAnalysis_Coordinates_withExc_test(){
        when(jdbcTemplate.query(anyString(), any(PreparedStatementSetter.class), any(RowMapper.class)))
                .thenReturn(Collections.singletonList(List.class));

        assertThrows(InvalidCoordinatesException.class, () -> {
            weatherService.getWeatherAnalysis("4170907", "44.79610");
        });
    }

    @Test
    public void getWeatherAnalysis_IPAddress_test(){
        when(jdbcTemplate.query(anyString(), any(PreparedStatementSetter.class), any(RowMapper.class)))
                .thenReturn(Collections.singletonList(List.class));

        assertDoesNotThrow(() -> {
            weatherService.getWeatherAnalysis("5.178.192.251");
        });
    }

    @Test
    public void getWeatherAnalysis_IPAddress_withExc_test(){
        when(jdbcTemplate.query(anyString(), any(PreparedStatementSetter.class), any(RowMapper.class)))
                .thenReturn(Collections.singletonList(List.class));

        assertThrows(InvalidIPAddressException.class, () -> {
            weatherService.getWeatherAnalysis("0.0.0.0");
        });
    }

    @Test
    public void getWeather_test() {
        when(ipAddressRequestRepository.save(any(IPAddressRequest.class)))
                .thenReturn(new IPAddressRequest());

        when(geolocationRepository.save(any(Geolocation.class)))
                .thenReturn(new Geolocation());

        when(weatherRepository.save(any(Weather.class)))
                .thenReturn(new Weather());


        when(restTemplate.exchange(ipGeolocationAPIURL+mockHttpServletRequest.getRemoteAddr(),
                HttpMethod.GET, null, GeolocationDto.class))
            .thenReturn(new ResponseEntity<>(geolocationDto, HttpStatus.OK));

        when(restTemplate.exchange(
                weatherAPIURL+geolocationDto.getLatitude()+","+geolocationDto.getLongitude(),
                HttpMethod.GET,
                null,
                WeatherDto.class))
            .thenReturn(new ResponseEntity<>(weatherDto, HttpStatus.OK));

        assertDoesNotThrow(() -> {
            weatherService.getWeather(mockHttpServletRequest);
        });
    }

    @Test
    public void test_FallBackMethod(){
        assertNotNull(weatherService.getWeatherFallBack(mockHttpServletRequest, new Exception()));
    }
}
