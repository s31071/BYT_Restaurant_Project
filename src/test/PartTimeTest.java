package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class PartTimeTest {

    private PartTime partTime;

    @BeforeEach
    void setUp() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method clearMethod = PartTime.class.getDeclaredMethod("clearExtent");
        clearMethod.setAccessible(true);
        clearMethod.invoke(null);
        partTime = new PartTime("Ewa", "Lis", "123456789", "Markowskiego", "Piaseczno","05-500", "Poland", "ewa@example.com",
                LocalDate.now(), Contract.B2B, Type.HALF_TIME);
    }

    @Test
    void testConstructor() {
        assertEquals("Ewa", partTime.getName());
        assertEquals("Lis", partTime.getSurname());
    }

    @Test
    void testCalculateSalaryHalfTime() {
        assertEquals(20 * 4.5 * partTime.getBaseSalary(),
                partTime.calculateSalary());
    }

    @Test
    void testInvalidPhone() {
        assertThrows(IllegalArgumentException.class, () ->
                new PartTime("Ewa", "Lis", "1234", "Markowskiego", "Piaseczno","05-500", "Poland", "ewa@example.com",
                        LocalDate.now(), Contract.B2B, Type.ON_CALL));
    }

    @Test
    void testNullPartTimeAdd() throws Exception {
        var method = PartTime.class.getDeclaredMethod("addExtent", PartTime.class);
        method.setAccessible(true);
        InvocationTargetException ex = assertThrows(InvocationTargetException.class,
                () -> method.invoke(null, (Object) null));

        assertTrue(ex.getTargetException() instanceof IllegalArgumentException);
        assertEquals("Part time cannot be null", ex.getTargetException().getMessage());
    }

    @Test
    void testAddExtentDuplicatePartTimeThrowsException() throws Exception {
        Method method = PartTime.class.getDeclaredMethod("addExtent", PartTime.class);
        method.setAccessible(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new PartTime("Ewa", "Lis", "123456789", "Markowskiego", "Piaseczno","05-500", "Poland", "ewa@example.com",
                        LocalDate.now(), Contract.B2B, Type.HALF_TIME
                )
        );

        assertEquals("Such part time employee is already in data base", ex.getMessage());
    }

    @Test
    void testExtentContainsOnlyOnePartTimeAfterDuplicateAttempt() throws Exception {
        Method addMethod = PartTime.class.getDeclaredMethod("addExtent", PartTime.class);
        addMethod.setAccessible(true);

        Method getExtentMethod = PartTime.class.getDeclaredMethod("getExtent");
        getExtentMethod.setAccessible(true);

        try {
            PartTime duplicate = new PartTime("Ewa", "Lis", "123456789", "Markowskiego", "Piaseczno","05-500", "Poland", "ewa@example.com",
                    LocalDate.now(), Contract.B2B, Type.HALF_TIME);
        } catch (IllegalArgumentException ignored) {}

        var extent = (java.util.List<PartTime>) getExtentMethod.invoke(null);
        assertEquals(1, extent.size());
        assertEquals(partTime, extent.get(0));
    }
}

