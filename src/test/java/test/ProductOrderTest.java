package test.java.test;

import classes.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ProductOrderTest {

    @Test
    void testConstructor_valid() {
        ProductOrder order = new ProductOrder(100, 5);
        assertEquals(100, order.getTotalPrice());
        assertEquals(5, order.getWeight());
    }

    @Test
    void testSetTotalPrice_valid() {
        ProductOrder order = new ProductOrder(50, 3);
        order.setTotalPrice(200);
        assertEquals(200, order.getTotalPrice());
    }

    @Test
    void testSetTotalPrice_negative_throwsException() {
        ProductOrder order = new ProductOrder(50, 3);
        assertThrows(IllegalArgumentException.class, () -> order.setTotalPrice(-10));
    }

    @Test
    void testSetWeight_valid() {
        ProductOrder order = new ProductOrder(50, 3);
        order.setWeight(10.0);
        assertEquals(10.0, order.getWeight());
    }

    @Test
    void testSetWeight_null_throwsException() {
        ProductOrder order = new ProductOrder(50, 3);
        assertThrows(IllegalArgumentException.class, () -> order.setWeight(null));
    }

    @Test
    void testSetWeight_nonPositive_throwsException() {
        ProductOrder order = new ProductOrder(50, 3);
        assertThrows(IllegalArgumentException.class, () -> order.setWeight(0.0));
        assertThrows(IllegalArgumentException.class, () -> order.setWeight(-2.0));
    }

    @Test
    void testAddSupplyHistory_addsItem() {
        ProductOrder order = new ProductOrder(50, 3);
        Address address = new Address("Koszykowa", "Warsaw", "00000", "Poland");
        Invoice invoice = new Invoice(Payment.PaymentMethod.CARD, 1, 111222333, "Emilia", address, order);

        SupplyHistory supplyHistory = new SupplyHistory(
                LocalDate.now(),
                SupplyHistory.Status.ORDERED,
                invoice,
                order
        );

        assertEquals(1, order.getSupplyHistoryList().size());
    }

    @Test
    void testAddSupplyHistory_null_doesNothing() {
        ProductOrder order = new ProductOrder(50, 3);
        order.addSupplyHistory(null);
        assertEquals(0, order.getSupplyHistoryList().size());
    }
}