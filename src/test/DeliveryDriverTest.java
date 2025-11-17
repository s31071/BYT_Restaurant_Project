package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class DeliveryDriverTest {

    private DeliveryDriver driver;

    @BeforeEach
    void setUp() {
        driver = new DeliveryDriver("Piotr", "Nowak", "987654321", "Lublin", "piotr@example.com",
                LocalDate.now(), Contract.mandateContract, "Fiat", "LU12345", true);
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
        double salary = driver.calculateSalary(Contract.EMPLOYMENT_CONTRACT, LocalDate.now());
        assertEquals(driver.getBaseSalary() * 168, salary);
    }

    @Test
    void testNullDriverAdd() throws Exception {
        var method = DeliveryDriver.class.getDeclaredMethod("addDeliveryDriver", DeliveryDriver.class);
        method.setAccessible(true);
        InvocationTargetException ex = assertThrows(InvocationTargetException.class,
                () -> method.invoke(null, (Object) null));

        assertTrue(ex.getTargetException() instanceof IllegalArgumentException);
        assertEquals("Delivery driver cannot be null", ex.getTargetException().getMessage());
    }
}

