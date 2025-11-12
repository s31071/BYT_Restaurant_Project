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
        address = new Address("Koszykowa", "Warsaw", "0000", "Poland");
        productOrder = new ProductOrder(2, 3);
        invoice = new Invoice(Payment.PaymentMethod.CARD, 1, 123456789, "Emilia", address, productOrder);
    }

    @Test
    void testConstructor_valid() {
        SupplyHistory supplyHistory = new SupplyHistory(
                LocalDate.now(),
                SupplyHistory.Status.ORDERED,
                invoice,
                productOrder
        );
        assertEquals(LocalDate.now(), supplyHistory.getDate());
        assertEquals(SupplyHistory.Status.ORDERED, supplyHistory.getStatus());
        assertEquals(invoice, supplyHistory.getInvoice());
        assertEquals(productOrder, supplyHistory.getProductOrder());
    }

    @Test
    void testSetDate_null_throwsException() {
        SupplyHistory supplyHistory = new SupplyHistory(
                LocalDate.now(),
                SupplyHistory.Status.ORDERED,
                invoice,
                productOrder
        );
        assertThrows(IllegalArgumentException.class, () -> supplyHistory.setDate(null));
    }

    @Test
    void testSetDate_future_throwsException() {
        SupplyHistory supplyHistory = new SupplyHistory(
                LocalDate.now(),
                SupplyHistory.Status.ORDERED,
                invoice,
                productOrder
        );
        assertThrows(IllegalArgumentException.class, () -> supplyHistory.setDate(LocalDate.now().plusDays(1)));
    }

    @Test
    void testSetStatus_null_throwsException() {
        SupplyHistory supplyHistory = new SupplyHistory(
                LocalDate.now(),
                SupplyHistory.Status.ORDERED,
                invoice,
                productOrder
        );
        assertThrows(IllegalArgumentException.class, () -> supplyHistory.setStatus(null));
    }

    @Test
    void testConstructor_nullInvoice_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new SupplyHistory(
                        LocalDate.now(),
                        SupplyHistory.Status.ORDERED,
                        null,
                        productOrder
                )
        );
    }

    @Test
    void testConstructor_nullProductOrder_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new SupplyHistory(
                        LocalDate.now(),
                        SupplyHistory.Status.ORDERED,
                        invoice,
                        null
                )
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