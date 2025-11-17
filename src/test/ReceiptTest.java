package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

//do zmiany po wieczornym callu
public class ReceiptTest {

    static class TestOrder extends Order {
        private final double totalPrice;

        public TestOrder(double totalPrice) {
            super(1, 1, null, 1, LocalDateTime.now(), new Table(1, 2, TableStatus.TAKEN, LocalDateTime.now()));
            this.totalPrice = totalPrice;
        }

        @Override
        public double getTotalPrice() {
            return totalPrice;
        }
    }

    private TestOrder order100;
    private TestOrder order50;
    private Receipt receiptNoTip;
    private Receipt receiptWithTip;

    @BeforeEach
    void setup() {
        order100 = new TestOrder(100);
        order50 = new TestOrder(50);

        receiptNoTip = new Receipt(Payment.PaymentMethod.CARD, order100);
        receiptWithTip = new Receipt(Payment.PaymentMethod.CARD, order100, 10.0);
    }

    @Test
    void testConstructorValidWithoutTip() {
        assertEquals(order100, receiptNoTip.getOrder());
        assertNull(receiptNoTip.getTip());
    }

    @Test
    void testConstructorValidWithTip() {
        assertEquals(10.0, receiptWithTip.getTip());
        assertEquals(order100, receiptWithTip.getOrder());
    }

    @Test
    void testSetTipValid() {
        Receipt r = new Receipt(Payment.PaymentMethod.CARD, order50);
        r.setTip(5.0);
        assertEquals(5.0, r.getTip());
    }

    @Test
    void testSetTipNullAllowed() {
        Receipt r = new Receipt(Payment.PaymentMethod.CARD, order50);
        r.setTip(null);
        assertNull(r.getTip());
    }

    @Test
    void testSetTipNegativeThrowsException() {
        Receipt r = new Receipt(Payment.PaymentMethod.CARD, order50);
        assertThrows(IllegalArgumentException.class, () -> r.setTip(-1.0));
    }

    @Test
    void testSetOrderNullThrowsException() {
        Receipt r = new Receipt(Payment.PaymentMethod.CARD, order50);
        assertThrows(IllegalArgumentException.class, () -> r.setOrder(null));
    }

    @Test
    void testGetFinalAmountWithoutTip() {
        Receipt r = new Receipt(Payment.PaymentMethod.CARD, order100, null);
        double expected = 100 + (100 * Receipt.service);
        assertEquals(expected, r.getFinalAmount());
    }

    @Test
    void testGetFinalAmountWithTip() {
        Receipt r = new Receipt(Payment.PaymentMethod.CARD, order100, 15.0);
        double expected = 100 + (100 * Receipt.service) + 15.0;
        assertEquals(expected, r.getFinalAmount());
    }

    @Test
    void testConstructorNullOrderThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Receipt(Payment.PaymentMethod.CARD, null));
    }

    @Test
    void testConstructorNullPaymentMethodThrowsException() {
        TestOrder order = new TestOrder(50);
        assertThrows(IllegalArgumentException.class,
                () -> new Receipt(null, order));
    }
}