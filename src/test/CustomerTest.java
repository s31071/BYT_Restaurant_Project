package test;

import classes.Address;
import classes.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private Customer c1;
    private Address address;

    @BeforeEach
    void setUp() {
        address = new Address("Markowskiego", "Piaseczno","05-500", "Poland");
        c1 = new Customer("Ewa", "Kowalska", "987654321", address, "ewa@example.com");
    }

    @Test
    void testConstructor() {
        assertEquals("Ewa", c1.getName());
        assertEquals("Kowalska", c1.getSurname());
    }

    @Test
    void testInvalidPhoneThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Customer("Ewa", "Kowalska", "12", address, "ewa@example.com"));
    }

    @Test
    void testInvalidEmailThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Customer("Ewa", "Kowalska", "123456789", address, "invalid"));
    }
}
