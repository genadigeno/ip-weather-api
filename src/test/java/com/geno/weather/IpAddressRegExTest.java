package com.geno.weather;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IpAddressRegExTest {
    private static final String VALID_IP_REGEX = "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$";
    private static final String RESERVED_IP_REGEX = "^(0\\.0\\.0\\.0|127|10|192\\.168|192\\.88\\.99).*$";

    @Test
    public void test_v1(){
        assertTrue("127.0.0.0".matches(VALID_IP_REGEX));
        assertTrue("0.0.0.0".matches(VALID_IP_REGEX));
        assertTrue("5.178.192.251".matches(VALID_IP_REGEX));

        assertFalse("07.0.0.0".matches(VALID_IP_REGEX));
        assertFalse("5.178.192.259".matches(VALID_IP_REGEX));
    }

    @Test
    public void test_v2(){
        assertFalse("5.178.192.251".matches(RESERVED_IP_REGEX));
        assertFalse("07.0.0.0".matches(RESERVED_IP_REGEX));
        assertFalse("5.178.192.259".matches(RESERVED_IP_REGEX));

        assertTrue("127.0.0.0".matches(RESERVED_IP_REGEX));
        assertTrue("0.0.0.0".matches(RESERVED_IP_REGEX));
    }
}
