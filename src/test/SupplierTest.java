package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SupplierTest {
    Supplier s;
    @BeforeEach
    void setUp() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method clearMethod = Supplier.class.getDeclaredMethod("clearExtent");
        clearMethod.setAccessible(true);
        clearMethod.invoke(null);
        s = new Supplier("Adam", "Nowak", "123456789", "Markowskiego", "Piaseczno","05-500", "Poland", "adam@example.com",
                "BestMeat", Category.MEAT, 120.0);
    }
    @Test
    void testConstructor() {

        assertEquals("Adam", s.getName());
        assertEquals("BestMeat", s.getCompanyName());
        assertEquals(Category.MEAT, s.getCategory());
    }

    @Test
    void shouldThrowExceptionForEmptyCompanyName() {
        assertThrows(IllegalArgumentException.class, () ->
                new Supplier("Adam", "Nowak", "123456789", "Markowskiego", "Piaseczno","05-500", "Poland", "adam@example.com",
                        "", Category.MEAT, 120.0));
    }

    @Test
    void shouldThrowExceptionForNullCompanyName() {
        assertThrows(IllegalArgumentException.class, () ->
                new Supplier("Adam", "Nowak", "123456789", "Markowskiego", "Piaseczno","05-500", "Poland", "adam@example.com",
                        null, Category.MEAT, 120.0));
    }

    @Test
    void shouldThrowExceptionForInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () ->
                new Supplier("Adam", "Nowak", "123456789", "Markowskiego", "Piaseczno","05-500", "Poland", "adam#example.com",
                        "BestMeat", Category.MEAT, 120.0));
    }

    @Test
    void testAddExtentDuplicateSupplierThrowsException() throws Exception {
        Method method = Supplier.class.getDeclaredMethod("addExtent", Supplier.class);
        method.setAccessible(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Supplier(
                        "Adam", "Nowak", "123456789", "Markowskiego", "Piaseczno","05-500", "Poland", "adam@example.com",
                        "BestMeat", Category.MEAT, 120.0
                )
        );

        assertEquals("Such supplier is already in data base", ex.getMessage());
    }

    @Test
    void testExtentContainsOnlyOneSupplierAfterDuplicateAttempt() throws Exception {
        Method addMethod = Supplier.class.getDeclaredMethod("addExtent", Supplier.class);
        addMethod.setAccessible(true);

        Method getExtentMethod = Supplier.class.getDeclaredMethod("getExtent");
        getExtentMethod.setAccessible(true);

        try {
            Supplier duplicate = new Supplier("Adam", "Nowak", "123456789", "Markowskiego", "Piaseczno","05-500", "Poland", "adam@example.com",
                    "BestMeat", Category.MEAT, 120.0);
        } catch (IllegalArgumentException ignored) {}

        var extent = (java.util.List<Supplier>) getExtentMethod.invoke(null);
        assertEquals(1, extent.size());
        assertEquals(s, extent.get(0));
    }
}
