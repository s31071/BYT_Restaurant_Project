package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class CookTest {

    private Cook cook;

    @BeforeEach
    void setUp() {
        cook = new Cook("Michal", "Kowal", "111222333", "Markowskiego", "Piaseczno","05-500", "Poland", "michal@example.com",
                LocalDate.now(), Contract.B2B, 8, "Head Chef", "French");
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
        var method = Cook.class.getDeclaredMethod("addExtent", Cook.class);
        method.setAccessible(true);
        InvocationTargetException ex = assertThrows(InvocationTargetException.class,
                () -> method.invoke(null, (Object) null));

        assertTrue(ex.getTargetException() instanceof IllegalArgumentException);
        assertEquals("Cook cannot be null", ex.getTargetException().getMessage());;
    }
}
