package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ProductOrderTest {

    private ProductOrder order;
    private Address address;
    private Invoice invoice;

    @BeforeEach
    void setup() {
        order = new ProductOrder(50, 3);
        address = new Address("Koszykowa", "Warsaw", "00000", "Poland");
        invoice = new Invoice(Payment.PaymentMethod.CARD, 1, 111222333, "Emilia", address, order);
    }

    @Test
    void testConstructorValid() {
        ProductOrder o = new ProductOrder(100, 5);
        assertEquals(100, o.getTotalPrice());
        assertEquals(5, o.getWeight());
    }

    @Test
    void testSetTotalPriceValid() {
        order.setTotalPrice(200);
        assertEquals(200, order.getTotalPrice());
    }

    @Test
    void testSetTotalPriceInvalidThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> order.setTotalPrice(-10));
    }

    @Test
    void testSetWeightValid() {
        order.setWeight(10.0);
        assertEquals(10.0, order.getWeight());
    }

    @Test
    void testSetWeightNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> order.setWeight(null));
    }

    @Test
    void testSetWeightNonPositiveThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> order.setWeight(0.0));
        assertThrows(IllegalArgumentException.class, () -> order.setWeight(-2.0));
    }

    @Test
    void testAddSupplyHistoryValid() {
        new SupplyHistory(
                LocalDate.now(),
                SupplyHistory.Status.ORDERED,
                invoice,
                order
        );

        assertEquals(1, order.getSupplyHistoryList().size());
    }

    @Test
    void testAddSupplyHistoryNullDoesNothing() {
        order.addSupplyHistory(null);
        assertEquals(0, order.getSupplyHistoryList().size());
    }
}