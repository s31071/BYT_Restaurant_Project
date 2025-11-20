package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class DeliveryDriverTest {

    private DeliveryDriver driver;
    @BeforeEach
    void setUp() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method clearMethod = DeliveryDriver.class.getDeclaredMethod("clearExtent");
        clearMethod.setAccessible(true);
        clearMethod.invoke(null);

        driver = new DeliveryDriver("Piotr", "Nowak", "987654321", "Markowskiego", "Piaseczno","05-500", "Poland", "piotr@example.com",
                LocalDate.now(), Contract.MANDATE_CONTRACT, "Fiat", "LU12345", true);
    }

    @Test
    void testConstructor() {
        assertEquals("Piotr", driver.getName());
        assertEquals("Nowak", driver.getSurname());
        assertEquals("Fiat", driver.getCarModel());
    }

    @Test
    void testCalculateSalaryNoKmBonus() {
        driver.setContract(Contract.EMPLOYMENT_CONTRACT);
        double salary = driver.calculateSalary();
        assertEquals(driver.getBaseSalary() * 168, salary);
    }

    @Test
    void testNullDriverAdd() throws Exception {
        var method = DeliveryDriver.class.getDeclaredMethod("addExtent", DeliveryDriver.class);
        method.setAccessible(true);
        InvocationTargetException ex = assertThrows(InvocationTargetException.class,
                () -> method.invoke(null, (Object) null));

        assertTrue(ex.getTargetException() instanceof IllegalArgumentException);
        assertEquals("Delivery driver cannot be null", ex.getTargetException().getMessage());
    }

    @Test
    void testAddExtentDuplicateDeliveryDriverThrowsException() throws Exception {
        Method method = DeliveryDriver.class.getDeclaredMethod("addExtent", DeliveryDriver.class);
        method.setAccessible(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new DeliveryDriver(
                        "Piotr", "Nowak", "987654321", "Markowskiego", "Piaseczno","05-500", "Poland", "piotr@example.com",
                        LocalDate.now(), Contract.MANDATE_CONTRACT, "Fiat", "LU12345", true
                )
        );

        assertEquals("Such delivery driver is already in data base", ex.getMessage());
    }

    @Test
    void testExtentContainsOnlyOneDeliveryDriverAfterDuplicateAttempt() throws Exception {
        Method addMethod = DeliveryDriver.class.getDeclaredMethod("addExtent", DeliveryDriver.class);
        addMethod.setAccessible(true);

        Method getExtentMethod = DeliveryDriver.class.getDeclaredMethod("getExtent");
        getExtentMethod.setAccessible(true);

        try {
            DeliveryDriver duplicate = new DeliveryDriver("Piotr", "Nowak", "987654321", "Markowskiego", "Piaseczno","05-500", "Poland", "piotr@example.com",
                    LocalDate.now(), Contract.MANDATE_CONTRACT, "Fiat", "LU12345", true);
        } catch (IllegalArgumentException ignored) {}

        var extent = (java.util.List<DeliveryDriver>) getExtentMethod.invoke(null);
        assertEquals(1, extent.size());
        assertEquals(driver, extent.get(0));
    }
}

