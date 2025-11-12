package test.java.test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class SupplyHistoryTest {

    private Address address;
    private ProductOrder productOrder;
    private Invoice invoice;

    @BeforeEach
    void setup() {
        address = new Address("Street 1", "City", "12345", "Country");
        productOrder = new ProductOrder(2, 3);
        invoice = new Invoice(Payment.PaymentMethod.CARD, 1, 123, "John", address, productOrder);
    }

    @Test
    void testConstructor_valid() {
        SupplyHistory sh = new SupplyHistory(
                LocalDate.now(),
                SupplyHistory.Status.ORDERED,
                invoice,
                productOrder
        );
        assertEquals(LocalDate.now(), sh.getDate());
        assertEquals(SupplyHistory.Status.ORDERED, sh.getStatus());
        assertEquals(invoice, sh.getInvoice());
        assertEquals(productOrder, sh.getProductOrder());
    }

    @Test
    void testSetDate_null_throwsException() {
        SupplyHistory sh = new SupplyHistory(
                LocalDate.now(),
                SupplyHistory.Status.ORDERED,
                invoice,
                productOrder
        );
        assertThrows(IllegalArgumentException.class, () -> sh.setDate(null));
    }

    @Test
    void testSetDate_future_throwsException() {
        SupplyHistory sh = new SupplyHistory(
                LocalDate.now(),
                SupplyHistory.Status.ORDERED,
                invoice,
                productOrder
        );
        assertThrows(IllegalArgumentException.class, () -> sh.setDate(LocalDate.now().plusDays(1)));
    }

    @Test
    void testSetStatus_null_throwsException() {
        SupplyHistory sh = new SupplyHistory(
                LocalDate.now(),
                SupplyHistory.Status.ORDERED,
                invoice,
                productOrder
        );
        assertThrows(IllegalArgumentException.class, () -> sh.setStatus(null));
    }

    @Test
    void testConstructor_nullInvoice_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new SupplyHistory(LocalDate.now(),
                        SupplyHistory.Status.ORDERED,
                        null,
                        productOrder)
        );
    }

    @Test
    void testConstructor_nullProductOrder_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new SupplyHistory(LocalDate.now(),
                        SupplyHistory.Status.ORDERED,
                        invoice,
                        null)
        );
    }

    @Test
    void testExtent_addsAutomatically() {
        int before = SupplyHistory.getSupplyHistoryList().size();
        new SupplyHistory(LocalDate.now(), SupplyHistory.Status.ORDERED, invoice, productOrder);
        int after = SupplyHistory.getSupplyHistoryList().size();
        assertEquals(before + 1, after);
    }
}