package com.geno.weather.error;

public class InvalidIPAddressException extends IllegalArgumentException {
    public InvalidIPAddressException() {
        super("Invalid IP Address");
    }
}
