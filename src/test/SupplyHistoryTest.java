package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class SupplyHistoryTest {

    private Address address;
    private ProductOrder productOrder;
    private Invoice invoice;
    private SupplyHistory supplyHistory;
    private LocalDate today;

    @BeforeEach
    void setup() {
        address = new Address("Koszykowa", "Warsaw", "0000", "Poland");
        productOrder = new ProductOrder(2, 3);
        invoice = new Invoice(Payment.PaymentMethod.CARD, 1, 123456789, "Emilia", address, productOrder);
        today = LocalDate.now();

        supplyHistory = new SupplyHistory(
                today,
                SupplyHistory.Status.ORDERED,
                invoice,
                productOrder
        );
    }

    @Test
    void testConstructorValid() {
        assertEquals(today, supplyHistory.getDate());
        assertEquals(SupplyHistory.Status.ORDERED, supplyHistory.getStatus());
        assertEquals(invoice, supplyHistory.getInvoice());
        assertEquals(productOrder, supplyHistory.getProductOrder());
    }

    @Test
    void testSetDateNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> supplyHistory.setDate(null));
    }

    @Test
    void testSetDateFutureThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> supplyHistory.setDate(today.plusDays(1)));
    }

    @Test
    void testSetStatusNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> supplyHistory.setStatus(null));
    }

    @Test
    void testConstructorNullInvoiceThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new SupplyHistory(today, SupplyHistory.Status.ORDERED, null, productOrder)
        );
    }

    @Test
    void testConstructorNullProductOrderThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new SupplyHistory(today, SupplyHistory.Status.ORDERED, invoice, null)
        );
    }

    @Test
    void testExtentAddsAutomatically() {
        int before = SupplyHistory.getSupplyHistoryList().size();
        new SupplyHistory(today, SupplyHistory.Status.ORDERED, invoice, productOrder);
        int after = SupplyHistory.getSupplyHistoryList().size();
        assertEquals(before + 1, after);
    }
    @Test
    void testConstructorNullStatusThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new SupplyHistory(LocalDate.now(), null, invoice, productOrder));
    }

    @Test
    void testConstructorNullDateThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new SupplyHistory(null, SupplyHistory.Status.ORDERED, invoice, productOrder));
    }

}