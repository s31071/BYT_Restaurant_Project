package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExtentToolBoxTest {

    @BeforeEach
    void cleanExtents() throws Exception {
        clearExtents();
    }

    private List<?> getExtentOf(Class<?> clazz) throws Exception {
        try {
            Field field = clazz.getDeclaredField("extent");
            field.setAccessible(true);
            Object value = field.get(null);

            if (value instanceof List<?> list) {
                return list;
            }
            return null;
        } catch (NoSuchFieldException ignored) {
            return null;
        }
    }

    private static final Class<?>[] extentClassess = {
            Person.class,
            Cook.class,
            Customer.class,
            DeliveryDriver.class,
            Dish.class,
            Invoice.class,
            Menu.class,
            Product.class,
            ProductOrder.class,
            Receipt.class,
            Reservation.class,
            Shift.class,
            Supplier.class,
            SupplyHistory.class,
            Table.class,
            Waiter.class
    };

    private void clearExtents() throws Exception {
        Method clearPerson = Person.class.getDeclaredMethod("clearExtent");
        clearPerson.setAccessible(true);
        clearPerson.invoke(null);

        Method clearCook = Cook.class.getDeclaredMethod("clearExtent");
        clearCook.setAccessible(true);
        clearCook.invoke(null);

        Method clearCustomer = Customer.class.getDeclaredMethod("clearExtent");
        clearCustomer.setAccessible(true);
        clearCustomer.invoke(null);

        Method clearDeliveryDriver = DeliveryDriver.class.getDeclaredMethod("clearExtent");
        clearDeliveryDriver.setAccessible(true);
        clearDeliveryDriver.invoke(null);

        Method clearDish = Dish.class.getDeclaredMethod("clearExtent");
        clearDish.setAccessible(true);
        clearDish.invoke(null);

        Method clearInvoice = Invoice.class.getDeclaredMethod("clearExtent");
        clearInvoice.setAccessible(true);
        clearInvoice.invoke(null);

        Method clearMenu = Menu.class.getDeclaredMethod("clearExtent");
        clearMenu.setAccessible(true);
        clearMenu.invoke(null);

        Method clearOrder = Order.class.getDeclaredMethod("clearExtent");
        clearOrder.setAccessible(true);
        clearOrder.invoke(null);

        Method clearProduct = Product.class.getDeclaredMethod("clearExtent");
        clearProduct.setAccessible(true);
        clearProduct.invoke(null);

        Method clearProductOrder = ProductOrder.class.getDeclaredMethod("clearExtent");
        clearProductOrder.setAccessible(true);
        clearProductOrder.invoke(null);

        Method clearReceipt = Receipt.class.getDeclaredMethod("clearExtent");
        clearReceipt.setAccessible(true);
        clearReceipt.invoke(null);

        Method clearReservation = Reservation.class.getDeclaredMethod("clearExtent");
        clearReservation.setAccessible(true);
        clearReservation.invoke(null);

        Method clearShift = Shift.class.getDeclaredMethod("clearExtent");
        clearShift.setAccessible(true);
        clearShift.invoke(null);

        Method clearSupplier = Supplier.class.getDeclaredMethod("clearExtent");
        clearSupplier.setAccessible(true);
        clearSupplier.invoke(null);

        Method clearSupplyHistory = SupplyHistory.class.getDeclaredMethod("clearExtent");
        clearSupplyHistory.setAccessible(true);
        clearSupplyHistory.invoke(null);

        Method clearTable = Table.class.getDeclaredMethod("clearExtent");
        clearTable.setAccessible(true);
        clearTable.invoke(null);

        Method clearWaiter = Waiter.class.getDeclaredMethod("clearExtent");
        clearWaiter.setAccessible(true);
        clearWaiter.invoke(null);
    }

    @Test
    void testExtentsAreInitiallyEmpty() throws Exception {
        for (Class<?> clazz : extentClassess) {
            List<?> extent = getExtentOf(clazz);
            if (extent == null) continue;
            assertEquals(0, extent.size(), clazz.getSimpleName() + " extent should be empty");
        }
    }

    @Test
    void testExtentsAreClearedCorrectly() throws Exception {
        new Product(11, "Milk", 2.0, Category.DAIRY, LocalDate.now().plusDays(2), 7.99);

        Person p = new Person(
                "Jan", "Kowalski", "123456789",
                "Street", "City", "00-001", "Poland",
                "jan@gmail.com"
        );
        new Customer(p);

        List<?> productExtent = getExtentOf(Product.class);
        assertFalse(productExtent.isEmpty());

        List<?> customerExtent = getExtentOf(Customer.class);
        assertFalse(customerExtent.isEmpty());

        List<?> personExtent = getExtentOf(Person.class);
        assertFalse(personExtent.isEmpty());

        clearExtents();

        for (Class<?> clazz : extentClassess) {
            List<?> extent = getExtentOf(clazz);
            if (extent == null) continue;
            assertEquals(0, extent.size(), clazz.getSimpleName() + " extent was not cleared!");
        }
    }

    @Test
    void testExtentClearingDoesNotThrow() {
        assertDoesNotThrow(() -> {
            try {
                clearExtents();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void testSaveAndLoadExtents() throws Exception {
        clearExtents();

        Person p = new Person(
                "Jan", "Kowalski", "123456789",
                "Street", "City", "00-001", "Poland",
                "jan@gmail.com"
        );
        new Customer(p);

        new Product(11, "Milk", 2.0, Category.DAIRY, LocalDate.now().plusDays(2), 7.99);

        assertEquals(1, getExtentOf(Customer.class).size());
        assertEquals(1, getExtentOf(Product.class).size());
        assertEquals(1, getExtentOf(Person.class).size());

        ExtentToolBox.saveAllExtents();
        clearExtents();

        assertEquals(0, getExtentOf(Customer.class).size());
        assertEquals(0, getExtentOf(Product.class).size());
        assertEquals(0, getExtentOf(Person.class).size());

        ExtentToolBox.loadAllExtents();

        assertEquals(1, getExtentOf(Customer.class).size());
        assertEquals(1, getExtentOf(Product.class).size());
        assertEquals(1, getExtentOf(Person.class).size());
    }

    @Test
    void testMultipleExtentsSavedAndLoaded() throws Exception {
        clearExtents();

        Person pc = new Person(
                "Adam", "Nowak", "987654321",
                "Long", "Town", "01-234", "Poland",
                "asd@gmail.com"
        );
        new Customer(pc);

        Person ps = new Person(
                "Ewa", "Nowak", "555444333",
                "Road", "City", "02-222", "Poland",
                "ewa@gmail.com"
        );
        new Supplier(ps, "SupplyCo", Category.MEAT, 20.0);

        Person pw = new Person(
                "Tom", "Wait", "333222111",
                "Street", "Village", "00-050", "Poland",
                "some@gmail.com"
        );

        new Employee(
                pw,
                LocalDate.now(),
                Contract.MANDATE_CONTRACT,
                null,
                Type.HALF_TIME,
                WorkwearSize.M,
                10
        );


        ExtentToolBox.saveAllExtents();
        clearExtents();
        ExtentToolBox.loadAllExtents();

        assertEquals(3, getExtentOf(Person.class).size());
        assertEquals(1, getExtentOf(Customer.class).size());
        assertEquals(1, getExtentOf(Supplier.class).size());
        assertEquals(1, getExtentOf(Waiter.class).size());
    }
}