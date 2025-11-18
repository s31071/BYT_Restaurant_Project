package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class FullTimeTest {

    private FullTime emp;
    private Address address;

    @BeforeEach
    void setUp() {
        address = new Address("Markowskiego", "Piaseczno","05-500", "Poland");
        emp = new FullTime("Jan", "Nowak", "123456789", address, "jan@example.com",
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
        var method = FullTime.class.getDeclaredMethod("addFullTime", FullTime.class);
        method.setAccessible(true);

        InvocationTargetException ex = assertThrows(InvocationTargetException.class,
                () -> method.invoke(null, (Object) null));

        Throwable cause = ex.getTargetException();
        assertTrue(cause instanceof IllegalArgumentException);
        assertEquals("Full time cannot be null", cause.getMessage());
    }
}
