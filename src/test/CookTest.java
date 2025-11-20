package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CookTest {

    private Cook cook;

    @BeforeEach
    void setUp() throws Exception {
        Method clearMethod = Cook.class.getDeclaredMethod("clearExtent");
        clearMethod.setAccessible(true);
        clearMethod.invoke(null);

        cook = new Cook("Michal", "Kowal", "111222333",
                "Markowskiego", "Piaseczno", "05-500", "Poland",
                "michal@example.com", LocalDate.now(), Contract.B2B,
                8, "Head Chef", "French");
    }

    @Test
    void testConstructor() {
        assertEquals("Michal", cook.getName());
        assertEquals("Kowal", cook.getSurname());
    }

    @Test
    void testCalculateSalaryZero() {
        assertEquals(8474.496, cook.calculateSalary());
    }

    @Test
    void testNullCookAdd() throws Exception {
        Method method = Cook.class.getDeclaredMethod("addExtent", Cook.class);
        method.setAccessible(true);

        InvocationTargetException ex = assertThrows(
                InvocationTargetException.class,
                () -> method.invoke(null, (Object) null)
        );

        assertTrue(ex.getTargetException() instanceof IllegalArgumentException);
        assertEquals("Cook cannot be null",
                ex.getTargetException().getMessage());
    }

    @Test
    void testAddExtentDuplicateCookThrowsException() throws Exception {
        Method method = Cook.class.getDeclaredMethod("addExtent", Cook.class);
        method.setAccessible(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Cook(
                        "Michal", "Kowal", "111222333",
                        "Markowskiego", "Piaseczno", "05-500", "Poland",
                        "michal@example.com", LocalDate.now(), Contract.B2B,
                        8, "Head Chef", "French"
                )
        );

        assertEquals("Such cook is already in data base", ex.getMessage());
    }

    @Test
    void testExtentContainsOnlyOneCookAfterDuplicateAttempt() throws Exception {
        Method addMethod = Cook.class.getDeclaredMethod("addExtent", Cook.class);
        addMethod.setAccessible(true);

        Method getExtentMethod = Cook.class.getDeclaredMethod("getExtent");
        getExtentMethod.setAccessible(true);

        try {
            Cook duplicate = new Cook("Michal", "Kowal", "111222333",
                    "Markowskiego", "Piaseczno", "05-500", "Poland",
                    "michal@example.com", LocalDate.now(), Contract.B2B,
                    8, "Head Chef", "French");
        } catch (IllegalArgumentException ignored) {}

        var extent = (java.util.List<Cook>) getExtentMethod.invoke(null);
        assertEquals(1, extent.size());
        assertEquals(cook, extent.get(0));
    }
}
