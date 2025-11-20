package test;

import classes.Payment;
import classes.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {
//we need a subclass to test abstract classes
    static class TestPayment extends Payment {

        private double sum;

        public TestPayment(PaymentMethod method, double sum) {
            super(method);
            if (sum < 0) {
                throw new IllegalArgumentException("Sum cannot be negative");
            }
            this.sum = sum;
        }

        @Override
        public void setSum() {
        }

        @Override
        public double getSum() {
            return sum;
        }
    }

    private TestPayment payment;

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
        assertThrows(IllegalArgumentException.class,
                () -> new TestPayment(null, 10.0));
    }

    @Test
    void testConstructorNegativeSumThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new TestPayment(PaymentMethod.CARD, -10.0));
    }

    @Test
    void testMethodPreservesStateAcrossChanges() {
        payment.setMethod(PaymentMethod.CARD);
        assertEquals(PaymentMethod.CARD, payment.getMethod());

        payment.setMethod(PaymentMethod.CASH);
        assertEquals(PaymentMethod.CASH, payment.getMethod());
    }

    @Test
    void testMultipleInstancesHoldIndependentValues() {
        TestPayment p1 = new TestPayment(PaymentMethod.CARD, 10);
        TestPayment p2 = new TestPayment(PaymentMethod.CASH, 20);

        assertNotEquals(p1.getSum(), p2.getSum());
        assertNotEquals(p1.getMethod(), p2.getMethod());
    }

    @Test
    void testSumGetterDoesNotChangeValue() {
        double s = payment.getSum();
        assertEquals(s, payment.getSum());
    }

    @Test
    void testSetSumDoesNotThrow() {
        assertDoesNotThrow(() -> payment.setSum());
    }

    @Test
    void testValidMethodEnumValues() {
        PaymentMethod[] methods = PaymentMethod.values();
        assertEquals(3, methods.length);

        assertTrue(containsMethod(methods, PaymentMethod.CARD));
        assertTrue(containsMethod(methods, PaymentMethod.CASH));
    }

    private boolean containsMethod(PaymentMethod[] methods, PaymentMethod target) {
        for (PaymentMethod m : methods) {
            if (m == target) return true;
        }
        return false;
    }

    @Test
    void testPaymentMethodValueOf() {
        assertEquals(PaymentMethod.CARD, PaymentMethod.valueOf("CARD"));
        assertEquals(PaymentMethod.CASH, PaymentMethod.valueOf("CASH"));
    }

    @Test
    void testPaymentMethodValueOfInvalid() {
        assertThrows(IllegalArgumentException.class, () -> PaymentMethod.valueOf("INVALID"));
    }
}