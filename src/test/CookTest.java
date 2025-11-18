package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class CookTest {

    private Cook cook;
    private Address address;

    @BeforeEach
    void setUp() {
        address = new Address("Markowskiego", "Piaseczno","05-500", "Poland");
        cook = new Cook("Michał", "Kowal", "111222333", address, "michal@example.com",
                LocalDate.now(), Contract.B2B, 8, "Head Chef", "French");
    }

    @Test
    void testConstructor() {
        assertEquals("Michał", cook.getName());
        assertEquals("Kowal", cook.getSurname());
    }

    @Test
    void testCalculateSalaryZero() {
        assertEquals(0, cook.calculateSalary(Contract.B2B, LocalDate.now()));
    }

    @Test
    void testNullCookAdd() throws Exception {
        var method = Cook.class.getDeclaredMethod("addCook", Cook.class);
        method.setAccessible(true);
        InvocationTargetException ex = assertThrows(InvocationTargetException.class,
                () -> method.invoke(null, (Object) null));

        assertTrue(ex.getTargetException() instanceof IllegalArgumentException);
        assertEquals("Cook cannot be null", ex.getTargetException().getMessage());;
    }
}
