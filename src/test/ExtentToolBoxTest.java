package test;

import classes.*;
import classes.Customer;
import classes.DeliveryDriver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExtentToolBoxTest {
    @BeforeEach
    void cleanExtents() throws Exception {
        clearExtents();
    }

    private List<?> getExtentOf(Class<?> clazz) throws Exception {
        Field field = clazz.getDeclaredField("extent");
        field.setAccessible(true);
        return (List<?>) field.get(null);
    }

    private static final Class<?>[] EXTENT_CLASSES = {
            Cook.class,
            Customer.class,
            DeliveryDriver.class,
            Dish.class,
            FullTime.class,
            Invoice.class,
            Menu.class,
            Order.class,
            PartTime.class,
            Product.class,
            ProductOrder.class,
            Receipt.class,
            Reservation.class,
            Shift.class,
            Supplier.class,
            SupplyHistory.class,
            Table.class,
            Waiter.class
    };

    private void clearExtents() throws Exception {
        for (Class<?> clazz : EXTENT_CLASSES) {
            try {
                Field extentField = clazz.getDeclaredField("extent");
                extentField.setAccessible(true);

                Object list = extentField.get(null);
                if (list instanceof List) {
                    ((List<?>) list).clear();
                }
            } catch (NoSuchFieldException ignored) {
            }
        }
    }

    @Test
    void testExtentsAreInitiallyEmpty() throws Exception {
        for (Class<?> clazz : EXTENT_CLASSES) {
            List<?> extent = getExtentOf(clazz);
            assertEquals(0, extent.size(), clazz.getSimpleName() + " extent should be empty");
        }
    }

    @Test
    void testExtentsAreClearedCorrectly() throws Exception {
        new Product(11, "Milk", 2.0, Category.DAIRY, LocalDate.now().plusDays(2), 7.99);
        new Customer("Jan", "Kowalski", "123456789",
                "Street", "City", "00-001", "Poland", "jan@example.com");

        assertFalse(getExtentOf(Product.class).isEmpty());
        assertFalse(getExtentOf(Customer.class).isEmpty());

        clearExtents();

        for (Class<?> clazz : EXTENT_CLASSES) {
            List<?> extent = getExtentOf(clazz);
            assertEquals(0, extent.size(), clazz.getSimpleName() + " extent was not cleared!");
        }
    }

    @Test
    void testExtentClearingDoesNotThrow() {
        assertDoesNotThrow(this::clearExtents);
    }
}
