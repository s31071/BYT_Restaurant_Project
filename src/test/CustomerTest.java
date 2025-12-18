package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private Customer c1;
    private Person p1;

    @BeforeEach
    void setUp() throws Exception {

        Method clearPerson = Person.class.getDeclaredMethod("clearExtent");
        clearPerson.setAccessible(true);
        clearPerson.invoke(null);

        Method clearCustomer = Customer.class.getDeclaredMethod("clearExtent");
        clearCustomer.setAccessible(true);
        clearCustomer.invoke(null);

        p1 = new Person(
                "Ewa", "Kowalska", "987654321",
                "Markowskiego", "Piaseczno", "05-500", "Poland",
                "ewa@gmail.com"
        );

        c1 = new Customer(p1);
    }

    @Test
    void testConstructor() {
        assertEquals("Ewa", c1.getName());
        assertEquals("Kowalska", c1.getSurname());
    }

    @Test
    void testInvalidPhoneThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Person("Ewa", "Kowalska", "12",
                        "Markowskiego", "Piaseczno", "05-500", "Poland",
                        "ewa@gmail.com"));
    }

    @Test
    void testInvalidEmailThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Person("Ewa", "Kowalska", "123456789",
                        "Markowskiego", "Piaseczno", "05-500", "Poland",
                        "invalid"));
    }

    @Test
    void testLoyaltyPointsInitialValue() {
        assertEquals(0.0, c1.getLoyaltyPoints());
    }

    @Test
    void testUpdateLoyaltyPointsAddsCorrectly() {
        c1.updateLoyaltyPoints(20.0);
        c1.updateLoyaltyPoints(30.0);
        assertEquals(50.0, c1.getLoyaltyPoints());
    }

    @Test
    void testUpdateLoyaltyPointsAcceptsNegative() {
        c1.updateLoyaltyPoints(100.0);
        c1.updateLoyaltyPoints(-40.0);
        assertEquals(60.0, c1.getLoyaltyPoints());
    }

    @Test
    void testAddExtentDuplicateCustomerThrowsException() {

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Customer(p1)
        );

        assertEquals("Such customer is already in data base", ex.getMessage());
    }

    @Test
    void testRemoveFromExtent() {

        Person p2 = new Person(
                "Eva", "Nowak", "555444333",
                "Koszykowa", "Warszawa", "00-001", "Poland",
                "eva@gmail.com"
        );

        Customer c2 = new Customer(p2);

        assertEquals(2, Customer.getExtent().size());

        Customer.removeFromExtent(c2);

        assertEquals(1, Customer.getExtent().size());
        assertTrue(Customer.getExtent().contains(c1));
    }

    @Test
    void testClearExtent() {

        Person p2 = new Person(
                "Eva", "Nowak", "555444333",
                "Koszykowa", "Warszawa", "00-001", "Poland",
                "eva@gmail.com"
        );

        new Customer(p2);

        assertEquals(2, Customer.getExtent().size());

        Customer.clearExtent();

        assertEquals(0, Customer.getExtent().size());
    }
}