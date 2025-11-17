package test;

import classes.Payment;
import classes.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {

    static class TestPayment extends Payment {
        private double sum;

        public TestPayment(PaymentMethod method, double sum) {
            super(method);
            if (sum < 0) throw new IllegalArgumentException("Sum cannot be negative");
            this.sum = sum;
        }

        @Override
        public double getSum() {
            return sum;
        }
    }

    private TestPayment payment;

    //tutaj nie clearuje extentu, bo jest abstract, więc go nie ma
    @BeforeEach
    void setup() {
        payment = new TestPayment(PaymentMethod.CARD, 50);
    }

    @Test
    void testConstructorValid() {
        TestPayment p = new TestPayment(PaymentMethod.CARD, 50);
        assertEquals(50, p.getSum());
        assertEquals(PaymentMethod.CARD, p.getMethod());
    }

    @Test
    void testSetPaymentMethodValid() {
        payment.setMethod(PaymentMethod.CASH);
        assertEquals(PaymentMethod.CASH, payment.getMethod());
    }

    @Test
    void testSetPaymentMethodNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> payment.setMethod(null));
    }

    @Test
    void testDerivedSumValid() {
        TestPayment p = new TestPayment(PaymentMethod.CARD, 99.5);
        assertEquals(99.5, p.getSum());
    }

    @Test
    void testConstructorNullPaymentMethodThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new TestPayment(null, 10.0));
    }

    @Test
    void testConstructorNegativeSumThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new TestPayment(PaymentMethod.CARD, -10.0));
    }
}