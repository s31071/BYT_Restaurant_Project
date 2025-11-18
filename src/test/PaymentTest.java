package test;

import classes.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {

    static class TestPayment extends Payment {
        public TestPayment(PaymentMethod method, double sum) {
            super(method, sum);
        }
    }

    private TestPayment payment;

    @BeforeEach
    void setup() {
        payment = new TestPayment(Payment.PaymentMethod.CARD, 50);
    }

    @Test
    void testConstructorValid() {
        TestPayment p = new TestPayment(Payment.PaymentMethod.CARD, 50);
        assertEquals(50, p.getSum());
        assertEquals(Payment.PaymentMethod.CARD, p.getPaymentMethod());
    }

    @Test
    void testSetPaymentMethodValid() {
        payment.setPaymentMethod(Payment.PaymentMethod.CASH);
        assertEquals(Payment.PaymentMethod.CASH, payment.getPaymentMethod());
    }

    @Test
    void testSetPaymentMethodNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> payment.setPaymentMethod(null));
    }

    @Test
    void testDerivedSumValid() {
        TestPayment p = new TestPayment(Payment.PaymentMethod.CARD, 99.5);
        assertEquals(99.5, p.getSum());
    }

    @Test
    void testConstructorNullPaymentMethodThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new TestPayment(null, 10.0));
    }

    @Test
    void testConstructorNegativeSumThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new TestPayment(Payment.PaymentMethod.CARD, -10.0));
    }
}