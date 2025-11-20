package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SupplyHistoryTest {

    private Address address;
    private ProductOrder productOrder;
    private Invoice invoice;
    private SupplyHistory supplyHistory;
    private LocalDate today;

    @BeforeEach
    void setup() throws Exception {

        Method clearProduct = Product.class.getDeclaredMethod("clearExtent");
        clearProduct.setAccessible(true);
        clearProduct.invoke(null);

        Method clearProductOrder = ProductOrder.class.getDeclaredMethod("clearExtent");
        clearProductOrder.setAccessible(true);
        clearProductOrder.invoke(null);

        Method clearInvoice = Invoice.class.getDeclaredMethod("clearExtent");
        clearInvoice.setAccessible(true);
        clearInvoice.invoke(null);

        Method clearSupplyHistory = SupplyHistory.class.getDeclaredMethod("clearExtent");
        clearSupplyHistory.setAccessible(true);
        clearSupplyHistory.invoke(null);

        Product p1 = new Product(1, "Milk", 1.0, Category.DAIRY, null, 10.0);
        Product p2 = new Product(2, "Bread", 0.5, Category.BREAD, null, 5.0);
        productOrder = new ProductOrder(List.of(p1, p2));

        address = new Address("Koszykowa", "Warsaw", "0000", "Poland");
        invoice = new Invoice(PaymentMethod.CARD, 1, 123456789, "Emilia", address, productOrder);

        today = LocalDate.now().minusDays(2);

        supplyHistory = new SupplyHistory(today, SupplyStatus.ORDERED, invoice, productOrder);
    }

    @Test
    void testConstructorValid() {
        assertEquals(today, supplyHistory.getDate());
        assertEquals(SupplyStatus.ORDERED, supplyHistory.getStatus());
        assertEquals(invoice, supplyHistory.getInvoice());
        assertEquals(productOrder, supplyHistory.getProductOrder());
    }

    @Test
    void testSetDateNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> supplyHistory.setDate(null));
    }

    @Test
    void testSetDateFutureThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> supplyHistory.setDate(today.plusDays(3)));
    }

    @Test
    void testSetStatusNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> supplyHistory.setStatus(null));
    }

    @Test
    void testConstructorNullInvoiceThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new SupplyHistory(today, SupplyStatus.ORDERED, null, productOrder));
    }

    @Test
    void testConstructorNullProductOrderThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new SupplyHistory(today, SupplyStatus.ORDERED, invoice, null));
    }

    @Test
    void testConstructorNullStatusThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new SupplyHistory(today, null, invoice, productOrder));
    }

    @Test
    void testConstructorNullDateThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new SupplyHistory(null, SupplyStatus.ORDERED, invoice, productOrder));
    }

    @Test
    void testDeliveredWithoutPreviousOrderedThrowsException() {
        Product p3 = new Product(3, "Butter", 0.3, Category.DAIRY, null, 8.0);
        Product p4 = new Product(4, "Cheese", 0.4, Category.DAIRY, null, 12.0);
        ProductOrder otherOrder = new ProductOrder(List.of(p3, p4));

        Address otherAddress = new Address("Test", "City", "1111", "Poland");
        Invoice otherInvoice = new Invoice(PaymentMethod.CARD, 2, 987654321, "Someone", otherAddress, otherOrder);

        assertThrows(IllegalArgumentException.class, () ->
                new SupplyHistory(today, SupplyStatus.DELIVERED, otherInvoice, otherOrder));
    }

    @Test
    void testDeliveredBeforeOrderedThrowsException() {
        LocalDate orderedDate = today;
        LocalDate deliveredBefore = today.minusDays(1);

        new SupplyHistory(orderedDate, SupplyStatus.ORDERED, invoice, productOrder);

        assertThrows(IllegalArgumentException.class, () ->
                new SupplyHistory(deliveredBefore, SupplyStatus.DELIVERED, invoice, productOrder));
    }

    @Test
    void testDeliveredValidAfterOrdered() {
        LocalDate orderedDate = today.minusDays(2);
        LocalDate deliveredDate = today.plusDays(1);

        new SupplyHistory(orderedDate, SupplyStatus.ORDERED, invoice, productOrder);

        SupplyHistory delivered = new SupplyHistory(deliveredDate, SupplyStatus.DELIVERED, invoice, productOrder);

        assertEquals(SupplyStatus.DELIVERED, delivered.getStatus());
    }

    @Test
    void testAddExtent() {
        SupplyHistory sh = new SupplyHistory(today, SupplyStatus.ORDERED, invoice, productOrder);
        assertTrue(SupplyHistory.getExtent().contains(sh));
    }

    @Test
    void testAddExtentWithNull() {
        assertThrows(IllegalArgumentException.class, () -> SupplyHistory.addExtent(null));
    }

    @Test
    void testGetExtentIsUnmodifiable() {
        SupplyHistory valid = new SupplyHistory(today, SupplyStatus.ORDERED, invoice, productOrder);
        assertThrows(UnsupportedOperationException.class, () ->
                SupplyHistory.getExtent().add(valid));
    }

    @Test
    void testRemoveFromExtent() {
        SupplyHistory sh = new SupplyHistory(today, SupplyStatus.ORDERED, invoice, productOrder);
        assertTrue(SupplyHistory.getExtent().contains(sh));

        SupplyHistory.removeFromExtent(sh);
        assertFalse(SupplyHistory.getExtent().contains(sh));
    }
}