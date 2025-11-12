package test.java.test;

import classes.Payment;
import classes.Receipt;
import classes.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ReceiptTest {

    static class TestOrder extends Order {
        private final double totalPrice;

        public TestOrder(double totalPrice) {
            super(1, 1, null, 1);
            this.totalPrice = totalPrice;
        }

        @Override
        public double getTotalPrice() {
            return totalPrice;
        }
    }

    @Test
    void testConstructor_validWithoutTip() {
        TestOrder order = new TestOrder(100);
        Receipt r = new Receipt(Payment.PaymentMethod.CARD, order);
        assertEquals(order, r.getOrder());
        assertNull(r.getTip());
    }

    @Test
    void testConstructor_validWithTip() {
        TestOrder order = new TestOrder(100);
        Receipt r = new Receipt(Payment.PaymentMethod.CARD, order, 10.0);
        assertEquals(10.0, r.getTip());
        assertEquals(order, r.getOrder());
    }

    @Test
    void testSetTip_valid() {
        TestOrder order = new TestOrder(50);
        Receipt r = new Receipt(Payment.PaymentMethod.CARD, order);
        r.setTip(5.0);
        assertEquals(5.0, r.getTip());
    }

    @Test
    void testSetTip_nullAllowed() {
        TestOrder order = new TestOrder(50);
        Receipt r = new Receipt(Payment.PaymentMethod.CARD, order);
        r.setTip(null);
        assertNull(r.getTip());
    }

    @Test
    void testSetTip_negative_throwsException() {
        TestOrder order = new TestOrder(50);
        Receipt r = new Receipt(Payment.PaymentMethod.CARD, order);
        assertThrows(IllegalArgumentException.class, () -> r.setTip(-1.0));
    }

    @Test
    void testSetOrder_null_throwsException() {
        TestOrder order = new TestOrder(50);
        Receipt r = new Receipt(Payment.PaymentMethod.CARD, order);
        assertThrows(IllegalArgumentException.class, () -> r.setOrder(null));
    }

    @Test
    void testGetFinalAmount_withoutTip() {
        TestOrder order = new TestOrder(100);
        Receipt r = new Receipt(Payment.PaymentMethod.CARD, order, null);
        double expected = 100 + (100 * Receipt.service);
        assertEquals(expected, r.getFinalAmount());
    }

    @Test
    void testGetFinalAmount_withTip() {
        TestOrder order = new TestOrder(100);
        Receipt r = new Receipt(Payment.PaymentMethod.CARD, order, 15.0);
        double expected = 100 + (100 * Receipt.service) + 15.0;
        assertEquals(expected, r.getFinalAmount());
    }
}