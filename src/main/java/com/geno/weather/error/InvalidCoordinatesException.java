package com.geno.weather.error;

public class InvalidCoordinatesException extends IllegalArgumentException {
    public InvalidCoordinatesException() {
        super("Coordinates are invalid");
    }
}
