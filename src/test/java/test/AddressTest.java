package test.java.test;

import classes.Address;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AddressTest {

    @Test
    void testConstructor_valid() {
        Address a = new Address("Koszykowa", "Warsaw", "0000", "Poland");

        assertEquals("Koszykowa", a.getStreet());
        assertEquals("Warsaw", a.getCity());
        assertEquals("0000", a.getPostalCode());
        assertEquals("Poland", a.getCountry());
    }

    @Test
    void testSetStreet_valid() {
        Address a = new Address("Koszykowa", "Warsaw", "0000", "Poland");
        a.setStreet("Nowowiejska");
        assertEquals("Nowowiejska", a.getStreet());
    }

    @Test
    void testSetStreet_invalid_throwsException() {
        Address a = new Address("Koszykowa", "Warsaw", "0000", "Poland");
        assertThrows(IllegalArgumentException.class, () -> a.setStreet(""));
        assertThrows(IllegalArgumentException.class, () -> a.setStreet(null));
    }

    @Test
    void testSetCity_valid() {
        Address a = new Address("Koszykowa", "Warsaw", "0000", "Poland");
        a.setCity("Krakow");
        assertEquals("Krakow", a.getCity());
    }

    @Test
    void testSetCity_invalid_throwsException() {
        Address a = new Address("Koszykowa", "Warsaw", "0000", "Poland");
        assertThrows(IllegalArgumentException.class, () -> a.setCity(""));
        assertThrows(IllegalArgumentException.class, () -> a.setCity(null));
    }

    @Test
    void testSetPostalCode_valid() {
        Address a = new Address("Koszykowa", "Warsaw", "0000", "Poland");
        a.setPostalCode("1111");
        assertEquals("1111", a.getPostalCode());
    }

    @Test
    void testSetPostalCode_invalid_throwsException() {
        Address a = new Address("Koszykowa", "Warsaw", "0000", "Poland");
        assertThrows(IllegalArgumentException.class, () -> a.setPostalCode(""));
        assertThrows(IllegalArgumentException.class, () -> a.setPostalCode(null));
    }

    @Test
    void testSetCountry_valid() {
        Address a = new Address("Koszykowa", "Warsaw", "0000", "Poland");
        a.setCountry("Germany");
        assertEquals("Germany", a.getCountry());
    }

    @Test
    void testSetCountry_invalid_throwsException() {
        Address a = new Address("Koszykowa", "Warsaw", "0000", "Poland");
        assertThrows(IllegalArgumentException.class, () -> a.setCountry(""));
        assertThrows(IllegalArgumentException.class, () -> a.setCountry(null));
    }
}