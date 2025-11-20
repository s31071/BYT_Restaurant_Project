package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class FullTimeTest {

    private FullTime emp;

    @BeforeEach
    void setUp() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method clearMethod = FullTime.class.getDeclaredMethod("clearExtent");
        clearMethod.setAccessible(true);
        clearMethod.invoke(null);
        emp = new FullTime("Jan", "Nowak", "123456789", "Markowskiego", "Piaseczno","05-500", "Poland", "jan@example.com",
                LocalDate.of(2020, 1, 1), Contract.EMPLOYMENT_CONTRACT);
    }

    @Test
    void testConstructor() {
        assertEquals("Jan", emp.getName());
        assertEquals("Nowak", emp.getSurname());
        assertEquals("123456789", emp.getPhoneNumber());
    }

    @Test
    void testCalculateSalaryEmploymentContract() {
        double salary = emp.calculateSalary();
        assertTrue(salary > 0);
    }

    @Test
    void testCalculateSalaryMandateContract() {
        double salary = emp.calculateSalary();
        assertTrue(salary > 0);
    }

    @Test
    void testNullEmployeeAdd() throws Exception {
        var method = FullTime.class.getDeclaredMethod("addExtent", FullTime.class);
        method.setAccessible(true);

        InvocationTargetException ex = assertThrows(InvocationTargetException.class,
                () -> method.invoke(null, (Object) null));

        Throwable cause = ex.getTargetException();
        assertTrue(cause instanceof IllegalArgumentException);
        assertEquals("Full time cannot be null", cause.getMessage());
    }
    @Test
    void testAddExtentDuplicateFullTimeThrowsException() throws Exception {
        Method method = FullTime.class.getDeclaredMethod("addExtent", FullTime.class);
        method.setAccessible(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new FullTime(
                        "Jan", "Nowak", "123456789", "Markowskiego", "Piaseczno","05-500", "Poland", "jan@example.com",
                        LocalDate.of(2020, 1, 1), Contract.EMPLOYMENT_CONTRACT
                )
        );

        assertEquals("Such full time employee is already in data base", ex.getMessage());
    }

    @Test
    void testExtentContainsOnlyOneFullTimeAfterDuplicateAttempt() throws Exception {
        Method addMethod = FullTime.class.getDeclaredMethod("addExtent", FullTime.class);
        addMethod.setAccessible(true);

        Method getExtentMethod = FullTime.class.getDeclaredMethod("getExtent");
        getExtentMethod.setAccessible(true);

        try {
            FullTime duplicate = new FullTime("Jan", "Nowak", "123456789", "Markowskiego", "Piaseczno","05-500", "Poland", "jan@example.com",
                    LocalDate.of(2020, 1, 1), Contract.EMPLOYMENT_CONTRACT);
        } catch (IllegalArgumentException ignored) {}

        var extent = (java.util.List<FullTime>) getExtentMethod.invoke(null);
        assertEquals(1, extent.size());
        assertEquals(emp, extent.get(0));
    }
}
