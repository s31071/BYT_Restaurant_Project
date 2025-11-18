package test;

import classes.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AddressTest {

    private Address address;

    @BeforeEach
    void setup() {
        address = new Address("Koszykowa", "Warsaw", "0000", "Poland");
    }

    @Test
    void testConstructorValid() {
        assertEquals("Koszykowa", address.getStreet());
        assertEquals("Warsaw", address.getCity());
        assertEquals("0000", address.getPostalCode());
        assertEquals("Poland", address.getCountry());
    }

    @Test
    void testSetStreetValid() {
        address.setStreet("Nowowiejska");
        assertEquals("Nowowiejska", address.getStreet());
    }

    @Test
    void testSetStreetInvalidThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> address.setStreet(""));
        assertThrows(IllegalArgumentException.class, () -> address.setStreet(null));
    }

    @Test
    void testSetCityValid() {
        address.setCity("Krakow");
        assertEquals("Krakow", address.getCity());
    }

    @Test
    void testSetCityInvalidThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> address.setCity(""));
        assertThrows(IllegalArgumentException.class, () -> address.setCity(null));
    }

    @Test
    void testSetPostalCodeValid() {
        address.setPostalCode("1111");
        assertEquals("1111", address.getPostalCode());
    }

    @Test
    void testSetPostalCodeInvalidThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> address.setPostalCode(""));
        assertThrows(IllegalArgumentException.class, () -> address.setPostalCode(null));
    }

    @Test
    void testSetCountryValid() {
        address.setCountry("Germany");
        assertEquals("Germany", address.getCountry());
    }

    @Test
    void testSetCountryInvalidThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> address.setCountry(""));
        assertThrows(IllegalArgumentException.class, () -> address.setCountry(null));
    }
    @Test
    void testConstructorNullStreetThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Address(null, "Warsaw", "0000", "Poland"));
    }

    @Test
    void testConstructorNullCityThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Address("Koszykowa", null, "0000", "Poland"));
    }

    @Test
    void testConstructorNullPostalCodeThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Address("Koszykowa", "Warsaw", null, "Poland"));
    }

    @Test
    void testConstructorNullCountryThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Address("Koszykowa", "Warsaw", "0000", null));
    }
}