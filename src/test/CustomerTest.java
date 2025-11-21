package test;

import classes.Address;
import classes.Contract;
import classes.Cook;
import classes.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private Customer c1;

    @BeforeEach
    void setUp() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method clearMethod = Customer.class.getDeclaredMethod("clearExtent");
        clearMethod.setAccessible(true);
        clearMethod.invoke(null);
        c1 = new Customer("Ewa", "Kowalska", "987654321", "Markowskiego", "Piaseczno","05-500", "Poland", "ewa@example.com");
    }

    @Test
    void testConstructor() {
        assertEquals("Ewa", c1.getName());
        assertEquals("Kowalska", c1.getSurname());
    }

    @Test
    void testInvalidPhoneThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Customer("Ewa", "Kowalska", "12", "Markowskiego", "Piaseczno","05-500", "Poland", "ewa@example.com"));
    }

    @Test
    void testInvalidEmailThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Customer("Ewa", "Kowalska", "123456789", "Markowskiego", "Piaseczno","05-500", "Poland", "invalid"));
    }

    @Test
    void testLoyaltyPointsInitialValue() {
        double points = c1.getLoyaltyPoints();
        assertEquals(0.0, points);
    }

    @Test
    void testUpdateLoyaltyPointsAddsCorrectly() throws Exception {
        Method updateMethod = Customer.class.getDeclaredMethod("updateLoyaltyPoints", double.class);
        updateMethod.setAccessible(true);
        updateMethod.invoke(c1, 20.0);
        updateMethod.invoke(c1, 30.0);
        double points = c1.getLoyaltyPoints();
        assertEquals(50.0, points);
    }

    @Test
    void testUpdateLoyaltyPointsAcceptsNegative() throws Exception {
        Method updateMethod = Customer.class.getDeclaredMethod("updateLoyaltyPoints", double.class);
        updateMethod.setAccessible(true);
        updateMethod.invoke(c1, 100.0);
        updateMethod.invoke(c1, -40.0);
        double points = c1.getLoyaltyPoints();
        assertEquals(60.0, points);
    }

    @Test
    void testAddExtentDuplicateCustomerThrowsException() throws Exception {
        Method method = Customer.class.getDeclaredMethod("addExtent", Customer.class);
        method.setAccessible(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Customer("Ewa", "Kowalska", "987654321", "Markowskiego", "Piaseczno","05-500", "Poland", "ewa@example.com"
                )
        );

        assertEquals("Such customer is already in data base", ex.getMessage());
    }

    @Test
    void testExtentContainsOnlyOneCustomerAfterDuplicateAttempt() throws Exception {
        Method addMethod = Customer.class.getDeclaredMethod("addExtent", Customer.class);
        addMethod.setAccessible(true);

        Method getExtentMethod = Customer.class.getDeclaredMethod("getExtent");
        getExtentMethod.setAccessible(true);

        try {
            Customer duplicate = new Customer("Ewa", "Kowalska", "987654321", "Markowskiego", "Piaseczno","05-500", "Poland", "ewa@example.com");
        } catch (IllegalArgumentException ignored) {}

        var extent = (java.util.List<Customer>) getExtentMethod.invoke(null);
        assertEquals(1, extent.size());
        assertEquals(c1, extent.get(0));
    }

    @Test
    void testRemoveFromExtent() throws Exception {
        Customer c2 = new Customer(
                "Eva", "Nowak", "555444333",
                "Koszykowa", "Warszawa", "00-001", "Poland",
                "eva@example.com"
        );

        Method getExtent = Customer.class.getDeclaredMethod("getExtent");
        getExtent.setAccessible(true);
        var extent = (java.util.List<Customer>) getExtent.invoke(null);

        assertEquals(2, extent.size());
        assertTrue(extent.contains(c2));

        Customer.removeFromExtent(c2);

        extent = (java.util.List<Customer>) getExtent.invoke(null);
        assertEquals(1, extent.size());
        assertFalse(extent.contains(c2));
        assertTrue(extent.contains(c1));
    }

    @Test
    void testClearExtent() throws Exception {
        Customer c2 = new Customer(
                "Eva", "Nowak", "555444333",
                "Koszykowa", "Warszawa", "00-001", "Poland",
                "s31431@pjwstk.pl"
        );

        Method getExtent = Customer.class.getDeclaredMethod("getExtent");
        getExtent.setAccessible(true);
        var extent = (java.util.List<Customer>) getExtent.invoke(null);

        assertEquals(2, extent.size());
        assertTrue(extent.contains(c1));
        assertTrue(extent.contains(c2));

        Method clearMethod = Customer.class.getDeclaredMethod("clearExtent");
        clearMethod.setAccessible(true);
        clearMethod.invoke(null);

        extent = (java.util.List<Customer>) getExtent.invoke(null);
        assertEquals(0, extent.size());
    }
}
