package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ReceiptTest {

    private Order order100;
    private Order order101;
    private Order order50;
    private Receipt receiptNoTip;
    private Receipt receiptWithTip;

    @BeforeEach
    void setup() {
        Table table = new Table(1, 2, TableStatus.TAKEN, LocalDateTime.now());

        order100 = new Order(1, 2, OrderStatus.TAKEN, 1, LocalDateTime.now(), table);
        order100.addDish(new Dish("DishA", 40.0));
        order100.addDish(new Dish("DishB", 60.0));

        order101 = new Order(1, 2, OrderStatus.TAKEN, 1, LocalDateTime.now(), table);
        order101.addDish(new Dish("DishA", 30.0));
        order101.addDish(new Dish("DishB", 80.0));

        order50 = new Order(2, 2, OrderStatus.TAKEN, 1, LocalDateTime.now(), table);
        order50.addDish(new Dish("DishX", 20.0));
        order50.addDish(new Dish("DishY", 30.0));

        receiptNoTip = new Receipt(PaymentMethod.CARD, order100);
        receiptWithTip = new Receipt(PaymentMethod.CARD, order101, Double.valueOf(10.0));
    }

    @Test
    void testConstructorValidWithoutTip() {
        assertEquals(order100, receiptNoTip.getOrder());
        assertNull(receiptNoTip.getTip());
    }

    @Test
    void testConstructorValidWithTip() {
        assertEquals(Double.valueOf(10.0), receiptWithTip.getTip());
        assertEquals(order101, receiptWithTip.getOrder());
    }

    @Test
    void testSetTipValid() {
        Receipt r = new Receipt(PaymentMethod.CARD, order50);
        r.setTip(Double.valueOf(5.0));
        assertEquals(Double.valueOf(5.0), r.getTip());
    }

    @Test
    void testSetTipNullAllowed() {
        Receipt r = new Receipt(PaymentMethod.CARD, order50);
        r.setTip(null);
        assertNull(r.getTip());
    }

    @Test
    void testSetTipNegativeThrowsException() {
        Receipt r = new Receipt(PaymentMethod.CARD, order50);
        assertThrows(IllegalArgumentException.class, () -> r.setTip(Double.valueOf(-1.0)));
    }

    @Test
    void testSetOrderNullThrowsException() {
        Receipt r = new Receipt(PaymentMethod.CARD, order50);
        assertThrows(IllegalArgumentException.class, () -> r.setOrder(null));
    }

    @Test
    void testSumWithoutTip() {
        Receipt r = new Receipt(PaymentMethod.CARD, order101, null);

        double expected = 110 + (110 * Receipt.service);
        assertEquals(expected, r.getSum(), 0.0001);
    }

    @Test
    void testSumWithTip() {
        Receipt r2 = new Receipt(PaymentMethod.CASH, order101, Double.valueOf(15.0));

        double expected = 110 + (110 * Receipt.service) + 15.0;
        assertEquals(expected, r2.getSum(), 0.0001);
    }

    @Test
    void testConstructorNullOrderThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Receipt(PaymentMethod.CARD, null));
    }

    @Test
    void testConstructorNullPaymentMethodThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Receipt(null, order50));
    }
}