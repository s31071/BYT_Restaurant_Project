package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class PartTimeTest {

    private PartTime partTime;
    private Address address;

    @BeforeEach
    void setUp() {
        address = new Address("Markowskiego", "Piaseczno","05-500", "Poland");
        partTime = new PartTime("Ewa", "Lis", "123456789", address, "ewa@example.com",
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
                new PartTime("Ewa", "Lis", "1234", address, "ewa@example.com",
                        LocalDate.now(), Contract.B2B, Type.ON_CALL));
    }

    @Test
    void testNullPartTimeAdd() throws Exception {
        var method = PartTime.class.getDeclaredMethod("addPartTime", PartTime.class);
        method.setAccessible(true);
        InvocationTargetException ex = assertThrows(InvocationTargetException.class,
                () -> method.invoke(null, (Object) null));

        assertTrue(ex.getTargetException() instanceof IllegalArgumentException);
        assertEquals("Part time cannot be null", ex.getTargetException().getMessage());
    }
}

