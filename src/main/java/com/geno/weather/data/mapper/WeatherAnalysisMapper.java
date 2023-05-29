package com.geno.weather.data.mapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class WeatherAnalysisMapper implements RowMapper<WeatherAnalysis> {

    @Override
    public WeatherAnalysis mapRow(ResultSet rs, int rowNum) throws SQLException {
        WeatherAnalysis wa = new WeatherAnalysis();
        wa.setIpAddress(rs.getString("ip_address"));
        wa.setDate(LocalDateTime.of(
                rs.getDate("created_on").toLocalDate(),
                rs.getTime("created_on").toLocalTime()));
        wa.setCountry(rs.getString("country"));
        wa.setCity(rs.getString("city"));
        wa.setTemperatureCelsius(rs.getFloat("temperature_celsius"));
        wa.setCondition(rs.getString("condition"));
        wa.setHumidity(rs.getInt("humidity"));
        wa.setWindKph(rs.getFloat("wind_kph"));
        wa.setWindMph(rs.getFloat("wind_mph"));
        return wa;
    }
}
