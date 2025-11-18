package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class WaiterTest {

    private Waiter waiter;
    private Address address;

    @BeforeEach
    void setUp() {
        address = new Address("Markowskiego", "Piaseczno","05-500", "Poland");
        waiter = new Waiter("Tomasz", "Lis", "123456789", address, "tomasz@example.com",
                LocalDate.now(), Contract.EMPLOYMENT_CONTRACT, WorkwearSize.M, 10);
    }

    @Test
    void testConstructor() {
        assertEquals("Tomasz", waiter.getName());
        assertEquals("Lis", waiter.getSurname());
        assertEquals(10, waiter.getMaximumTables());
    }

    @Test
    void testCalculateSalaryAlwaysZero() {
        assertEquals(0, waiter.calculateSalary());
    }

    @Test
    void testNullWaiterAdd() throws Exception {
        var method = Waiter.class.getDeclaredMethod("addWaiter", Waiter.class);
        method.setAccessible(true);
        InvocationTargetException ex = assertThrows(InvocationTargetException.class,
                () -> method.invoke(null, (Object) null));

        assertTrue(ex.getTargetException() instanceof IllegalArgumentException);
        assertEquals("Waiter cannot be null", ex.getTargetException().getMessage());
    }
}

