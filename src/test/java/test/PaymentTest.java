package test.java.test;

import classes.Payment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {

    static class TestPayment extends Payment {
        public TestPayment(PaymentMethod method, double sum) {
            super(method, sum);
        }
    }

    @Test
    void testConstructor_valid() {
        TestPayment p = new TestPayment(Payment.PaymentMethod.CARD, 50);
        assertEquals(50, p.getSum());
        assertEquals(Payment.PaymentMethod.CARD, p.getPaymentMethod());
    }

    @Test
    void testSetPaymentMethod_valid() {
        TestPayment p = new TestPayment(Payment.PaymentMethod.CARD, 10);
        p.setPaymentMethod(Payment.PaymentMethod.CASH);
        assertEquals(Payment.PaymentMethod.CASH, p.getPaymentMethod());
    }

    @Test
    void testSetPaymentMethod_null_throwsException() {
        TestPayment p = new TestPayment(Payment.PaymentMethod.CARD, 10);
        assertThrows(IllegalArgumentException.class, () -> p.setPaymentMethod(null));
    }

    @Test
    void testDerivedSum_noSetter() {
        TestPayment p = new TestPayment(Payment.PaymentMethod.CARD, 99.5);
        assertEquals(99.5, p.getSum());
    }
}