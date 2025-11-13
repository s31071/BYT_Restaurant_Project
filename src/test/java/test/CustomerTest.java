package test.java.test;

import classes.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private Customer c1;

    @BeforeEach
    void setUp() {
        Customer.getPersonList().clear();
        c1 = new Customer("Ewa", "Kowalska", "987654321", "Poznań", "ewa@example.com");
    }

    @Test
    void testConstructor() {
        assertEquals("Ewa", c1.getName());
        assertEquals("Kowalska", c1.getSurname());
    }

    @Test
    void testInvalidPhoneThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Customer("Ewa", "Kowalska", "12", "Poznań", "ewa@example.com"));
    }

    @Test
    void testInvalidEmailThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Customer("Ewa", "Kowalska", "123456789", "Poznań", "invalid"));
    }
}
