package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.testng.AssertJUnit.assertEquals;

class FullTimeTest {

    private FullTime emp;

    @BeforeEach
    void setUp() throws Exception {
        Method clearMethod = FullTime.class.getDeclaredMethod("clearExtent");
        clearMethod.setAccessible(true);
        clearMethod.invoke(null);
        emp = new FullTime("Jan", "Nowak", "123456789", "Markowskiego", "Piaseczno","05-500", "Poland", "jan@example.com",
                LocalDate.of(2020, 1, 1), Contract.EMPLOYMENT_CONTRACT, null);
    }

    @org.testng.annotations.Test
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
    void testCalculateSalaryB2BContract() throws Exception {
        FullTime ft = new FullTime("Adam", "Nowak", "987654321", "Kwiatowa", "Warszawa", "00-001",
                "Poland", "adam@example.com", LocalDate.of(2018, 1, 1), Contract.B2B, null);
        assertTrue(ft.calculateSalary() > 0);
    }

    @Test
    void testCalculateSalaryIncreasesWithYearsWorked() throws Exception {
        FullTime older = new FullTime("Anna", "Zielinska", "555666777", "Lesna", "Krakow", "30-001",
                "Poland", "anna@example.com", LocalDate.of(2010, 1, 1), Contract.EMPLOYMENT_CONTRACT, null);
        double s1 = older.calculateSalary();
        FullTime newer = new FullTime("Piotr", "Lewandowski", "222333444", "Polna", "Gdansk", "80-001",
                "Poland", "piotr@example.com", LocalDate.of(2023, 1, 1), Contract.EMPLOYMENT_CONTRACT, null);
        double s2 = newer.calculateSalary();
        assertTrue(s1 > s2);
    }

    @Test
    void testConstructorAllFieldsSetCorrectly() throws Exception {
        FullTime f = new FullTime("Kamil", "Kowalski", "111222333", "Dluga", "Lublin",
                "20-001", "Poland", "kamil@example.com", LocalDate.of(2019, 5, 10), Contract.MANDATE_CONTRACT, null);
        assertEquals("Kamil", f.getName());
        assertEquals("Kowalski", f.getSurname());
        assertEquals("111222333", f.getPhoneNumber());
    }

    @Test
    void testCalculateSalaryZeroIfJustEmployed() throws Exception {
        FullTime f = new FullTime("Marek", "Nowak", "777888999", "Krotka", "Poznan",
                "60-001", "Poland", "marek@example.com", LocalDate.now(), Contract.B2B, null);
        assertEquals(0, f.calculateSalary());
    }
    @Test
    void testAddExtentDuplicateFullTimeThrowsException() throws Exception {
        Method method = FullTime.class.getDeclaredMethod("addExtent", FullTime.class);
        method.setAccessible(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new FullTime(
                        "Jan", "Nowak", "123456789", "Markowskiego", "Piaseczno","05-500", "Poland", "jan@example.com",
                        LocalDate.of(2020, 1, 1), Contract.EMPLOYMENT_CONTRACT, null
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
                    LocalDate.of(2020, 1, 1), Contract.EMPLOYMENT_CONTRACT, null);
        } catch (IllegalArgumentException ignored) {}

        var extent = (java.util.List<FullTime>) getExtentMethod.invoke(null);
        assertEquals(1, extent.size());
        assertEquals(emp, extent.get(0));
    }
}
