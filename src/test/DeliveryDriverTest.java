package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    void testSetCarModelValid() {
        assertDoesNotThrow(() -> driver.setCarModel("Toyota"));
    }

    @Test
    void testSetCarModelInvalidNull() {
        assertThrows(IllegalArgumentException.class, () -> driver.setCarModel(null));
    }

    @Test
    void testSetCarModelInvalidBlank() {
        assertThrows(IllegalArgumentException.class, () -> driver.setCarModel(" "));
    }

    @Test
    void testSetRegistrationNumberValid() {
        assertDoesNotThrow(() -> driver.setRegistrationNumber("WA1234A"));
        assertDoesNotThrow(() -> driver.setRegistrationNumber("LU-3456"));
    }

    @Test
    void testSetRegistrationNumberInvalidEmpty() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> driver.setRegistrationNumber("")
        );
        assertEquals("Registration number cannot be empty", ex.getMessage());
    }

    @Test
    void testSetRegistrationNumberInvalidFormat() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> driver.setRegistrationNumber("invalid-plate")
        );
        assertEquals("Invalid Polish registration number format", ex.getMessage());
    }

    @Test
    void testSetKmsInDayValid() {
        assertDoesNotThrow(() -> driver.setKmsInDay(120.5));
    }

    @Test
    void testSetKmsInDayNegativeThrows() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> driver.setKmsInDay(-5)
        );
        assertEquals("Kms driven in a day cannot be negative", ex.getMessage());
    }

    @Test
    void testSetKmsInMonthValid() {
        assertDoesNotThrow(() -> driver.setKmsInMonth(421));
    }

    @Test
    void testSetKmsInMonthNegativeThrows() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> driver.setKmsInMonth(-50)
        );
        assertEquals("Kms driven in a month cannot be negative", ex.getMessage());
    }

    @Test
    void testCalculateSalaryWithKmBonus() {
        driver.setKmsInMonth(500);
        driver.setBonusApply(true);
        driver.setContract(Contract.EMPLOYMENT_CONTRACT);
        double expected = driver.getBaseSalary() * 168 + 500;
        assertEquals(expected, driver.calculateSalary());
    }

    @Test
    void testCalculateSalaryWithoutKmBonus() {
        driver.setKmsInMonth(500);
        driver.setBonusApply(false);
        driver.setContract(Contract.EMPLOYMENT_CONTRACT);
        double expected = driver.getBaseSalary() * 168;
        assertEquals(expected, driver.calculateSalary());
    }

    @Test
    void testConfirmDeliveryUsingReflection() throws Exception {
        Order order = new Order(1, 3, OrderStatus.READY, LocalDateTime.now());
        Method method = DeliveryDriver.class.getDeclaredMethod("confirmDelivery", Order.class);
        method.setAccessible(true);
        method.invoke(driver, order);
        assertEquals(OrderStatus.DELIVERED, order.getStatus());
    }

    @Test
    void testSaveDailyKmsUsingReflection() throws Exception {
        driver.setKmsInDay(100);
        Method method = DeliveryDriver.class.getDeclaredMethod("saveDailyKms");
        method.setAccessible(true);
        method.invoke(driver);
        assertEquals(100, driver.getKmsInMonth());
        assertEquals(0, driver.getKmsInDay());
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

