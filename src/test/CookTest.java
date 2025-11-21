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
                "michal@gmail.com", LocalDate.now(), Contract.B2B,
                8, "Head Chef", "French");
    }
    @Test
    void testSetYearsOfExperienceValid() {
        cook.setYearsOfExperience(5);
        assertDoesNotThrow(() -> cook.setYearsOfExperience(10));
    }

    @Test
    void testSetYearsOfExperienceInvalidNegative() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cook.setYearsOfExperience(-1)
        );
        assertEquals("Years of experience cannot be negative", ex.getMessage());
    }

    @Test
    void testSetTitleValid() {
        assertDoesNotThrow(() -> cook.setTitle("Sous Chef"));
    }

    @Test
    void testSetTitleInvalidNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cook.setTitle(null)
        );
        assertEquals("Title cannot be empty", ex.getMessage());
    }

    @Test
    void testSetTitleInvalidBlank() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cook.setTitle(" ")
        );
        assertEquals("Title cannot be empty", ex.getMessage());
    }

    @Test
    void testSetSpecializationValid() {
        assertDoesNotThrow(() -> cook.setSpecialization("Italian"));
    }

    @Test
    void testSetSpecializationInvalidNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cook.setSpecialization(null)
        );
        assertEquals("Specialization cannot be empty", ex.getMessage());
    }

    @Test
    void testSetSpecializationInvalidBlank() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cook.setSpecialization(" ")
        );
        assertEquals("Specialization cannot be empty", ex.getMessage());
    }

    @Test
    void testConstructor() {
        assertEquals("Michal", cook.getName());
        assertEquals("Kowal", cook.getSurname());
    }

    @Test
    void testCalculateSalary() {
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
                        "michal@gmail.com", LocalDate.now(), Contract.B2B,
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
                    "michal@gmail.com", LocalDate.now(), Contract.B2B,
                    8, "Head Chef", "French");
        } catch (IllegalArgumentException ignored) {}

        var extent = (java.util.List<Cook>) getExtentMethod.invoke(null);
        assertEquals(1, extent.size());
        assertEquals(cook, extent.get(0));
    }

    @Test
    void testRemoveFromExtentWorksProperly() throws Exception {
        Cook c2 = new Cook(
                "Adam", "Nowak", "999888777",
                "Koszykowa", "Warszawa", "00-000", "Poland",
                "s31431@pjwstk.edu.pl", LocalDate.now(), Contract.B2B,
                4, "Sous Chef", "Italian"
        );

        Method getExtent = Cook.class.getDeclaredMethod("getExtent");
        getExtent.setAccessible(true);

        var extent = (java.util.List<Cook>) getExtent.invoke(null);
        assertEquals(2, extent.size());
        assertTrue(extent.contains(c2));

        Cook.removeFromExtent(c2);

        extent = (java.util.List<Cook>) getExtent.invoke(null);
        assertEquals(1, extent.size());
        assertFalse(extent.contains(c2));
        assertTrue(extent.contains(cook));
    }

    @Test
    void testClearExtentWorksProperly() throws Exception {
        Cook c2 = new Cook(
                "Eva", "Nowak", "555444333",
                "Koszykowa", "Warszawa", "00-001", "Poland",
                "s31431@pjwstk.edu.pl", LocalDate.now(), Contract.B2B,
                6, "Pastry Chef", "Desserts"
        );

        Cook c3 = new Cook(
                "Adam", "Kowalski", "111555999",
                "Nowogrodzka", "Warszawa", "00-002", "Poland",
                "decemilka@gmail.com", LocalDate.now(), Contract.B2B,
                3, "Junior Chef", "Breakfast"
        );

        Method getExtent = Cook.class.getDeclaredMethod("getExtent");
        getExtent.setAccessible(true);

        var extent = (java.util.List<Cook>) getExtent.invoke(null);
        assertEquals(3, extent.size());
        assertTrue(extent.contains(c2));
        assertTrue(extent.contains(c3));
        assertTrue(extent.contains(cook));

        Method clear = Cook.class.getDeclaredMethod("clearExtent");
        clear.setAccessible(true);
        clear.invoke(null);

        extent = (java.util.List<Cook>) getExtent.invoke(null);
        assertEquals(0, extent.size());
    }
}
