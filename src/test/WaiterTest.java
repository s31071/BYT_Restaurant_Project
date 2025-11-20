package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class WaiterTest {

    private Waiter waiter;

    @BeforeEach
    void setUp() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method clearMethod = Waiter.class.getDeclaredMethod("clearExtent");
        clearMethod.setAccessible(true);
        clearMethod.invoke(null);
        waiter = new Waiter("Tomasz", "Lis", "123456789", "Markowskiego", "Piaseczno","05-500", "Poland", "tomasz@example.com",
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
        assertEquals(5492.0, waiter.calculateSalary());
    }

    @Test
    void testNullWaiterAdd() throws Exception {
        var method = Waiter.class.getDeclaredMethod("addExtent", Waiter.class);
        method.setAccessible(true);
        InvocationTargetException ex = assertThrows(InvocationTargetException.class,
                () -> method.invoke(null, (Object) null));

        assertTrue(ex.getTargetException() instanceof IllegalArgumentException);
        assertEquals("Waiter cannot be null", ex.getTargetException().getMessage());
    }

    @Test
    void shouldThrowExceptionForNullWorkwearSize() {
        assertThrows(IllegalArgumentException.class, () ->
                new Waiter("Tomasz", "Lis", "123456789", "Markowskiego", "Piaseczno", "05-500",
                        "Poland", "tomasz@example.com", LocalDate.now(),
                        Contract.EMPLOYMENT_CONTRACT, null, 10));
    }

    @Test
    void shouldThrowExceptionForZeroMaximumTables() {
        assertThrows(IllegalArgumentException.class, () ->
                new Waiter("Tomasz", "Lis", "123456789", "Markowskiego", "Piaseczno", "05-500",
                        "Poland", "tomasz@example.com", LocalDate.now(),
                        Contract.EMPLOYMENT_CONTRACT, WorkwearSize.M, 0));
    }

    @Test
    void shouldThrowExceptionForNegativeMaximumTables() {
        assertThrows(IllegalArgumentException.class, () ->
                new Waiter("Tomasz", "Lis", "123456789", "Markowskiego", "Piaseczno", "05-500",
                        "Poland", "tomasz@example.com", LocalDate.now(),
                        Contract.EMPLOYMENT_CONTRACT, WorkwearSize.M, -5));
    }

    @Test
    void shouldThrowExceptionForTooManyTables() {
        assertThrows(IllegalArgumentException.class, () ->
                new Waiter("Tomasz", "Lis", "123456789", "Markowskiego", "Piaseczno", "05-500",
                        "Poland", "tomasz@example.com", LocalDate.now(),
                        Contract.EMPLOYMENT_CONTRACT, WorkwearSize.M, 30));
    }

    @Test
    void testSetWorkwearSizeValid() {
        waiter.setWorkwearSize(WorkwearSize.L);
        assertEquals(WorkwearSize.L, waiter.getWorkwearSize());
    }

    @Test
    void testSetMaximumTablesValidLowerRange() {
        waiter.setMaximumTables(1);
        assertEquals(1, waiter.getMaximumTables());
    }

    @Test
    void testSetMaximumTablesValid() {
        waiter.setMaximumTables(15);
        assertEquals(15, waiter.getMaximumTables());
    }

    @Test
    void testCalculateSalaryIncreasesWithTables() {
        double baseSalary = waiter.calculateSalary();
        waiter.setMaximumTables(20);
        double higherSalary = waiter.calculateSalary();
        assertTrue(higherSalary > baseSalary);
    }

    @Test
    void testCalculateSalaryIncreasesWithYearsWorked() {
        Waiter w = new Waiter("Anna", "Kowal", "987654321", "Dluga", "Warszawa", "00-001",
                "Poland", "anna@example.com", LocalDate.now().minusYears(5),
                Contract.EMPLOYMENT_CONTRACT, WorkwearSize.S, 10);

        double salary = w.calculateSalary();
        assertTrue(salary > waiter.calculateSalary());
    }

    @Test
    void testAddExtentDuplicateWaiterThrowsException() throws Exception {
        Method method = Waiter.class.getDeclaredMethod("addExtent", Waiter.class);
        method.setAccessible(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Waiter(
                        "Tomasz", "Lis", "123456789", "Markowskiego", "Piaseczno","05-500", "Poland", "tomasz@example.com",
                        LocalDate.now(), Contract.EMPLOYMENT_CONTRACT, WorkwearSize.M, 10
                )
        );

        assertEquals("Such waiter is already in data base", ex.getMessage());
    }

    @Test
    void testExtentContainsOnlyOneWaiterAfterDuplicateAttempt() throws Exception {
        Method addMethod = Waiter.class.getDeclaredMethod("addExtent", Waiter.class);
        addMethod.setAccessible(true);

        Method getExtentMethod = Waiter.class.getDeclaredMethod("getExtent");
        getExtentMethod.setAccessible(true);

        try {
            Waiter duplicate = new Waiter("Tomasz", "Lis", "123456789", "Markowskiego", "Piaseczno","05-500", "Poland", "tomasz@example.com",
                    LocalDate.now(), Contract.EMPLOYMENT_CONTRACT, WorkwearSize.M, 10);
        } catch (IllegalArgumentException ignored) {}

        var extent = (java.util.List<Waiter>) getExtentMethod.invoke(null);
        assertEquals(1, extent.size());
        assertEquals(waiter, extent.get(0));
    }
}

