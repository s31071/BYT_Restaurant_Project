package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReceiptTest {

    private Order order100;
    private Order order101;
    private Order order50;
    private Receipt receiptNoTip;
    private Receipt receiptWithTip;
    private List<Integer> reviews = new ArrayList<>(List.of(4, 5, 4, 5, 5, 3));

    @BeforeEach
    void setup() throws Exception {

        Method clearReceipt = Receipt.class.getDeclaredMethod("clearExtent");
        clearReceipt.setAccessible(true);
        clearReceipt.invoke(null);

        Method clearOrder = Order.class.getDeclaredMethod("clearExtent");
        clearOrder.setAccessible(true);
        clearOrder.invoke(null);

        Method clearDish = Dish.class.getDeclaredMethod("clearExtent");
        clearDish.setAccessible(true);
        clearDish.invoke(null);

        Method clearTable = Table.class.getDeclaredMethod("clearExtent");
        clearTable.setAccessible(true);
        clearTable.invoke(null);

        Table table = new Table(1, 2, TableStatus.TAKEN, LocalDateTime.now());

        Dish dishA = new Dish("DishA", 40.0, reviews);
        Dish dishB = new Dish("DishB", 60.0, reviews);
        order100 = new Order(1, 2, OrderStatus.TAKEN, LocalDateTime.now(), table);
        order100.addDish(dishA, 1);
        order100.addDish(dishB, 1);

        order101 = new Order(1, 2, OrderStatus.TAKEN, LocalDateTime.now(), table);
        Dish dishC = new Dish("DishC", 30.0, reviews);
        Dish dishD = new Dish("DishD", 80.0, reviews);
        order101.addDish(dishC, 1);
        order101.addDish(dishD, 1);

        order50 = new Order(2, 2, OrderStatus.TAKEN, LocalDateTime.now(), table);
        Dish dishX = new Dish("DishX", 20.0, reviews);
        Dish dishY = new Dish("DishY", 30.0, reviews);
        order50.addDish(dishX, 1);
        order50.addDish(dishY, 1);

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
        assertThrows(IllegalArgumentException.class, () -> r.setTip(-1.0));
    }

    @Test
    void testConstructorNegativeTipThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Receipt(PaymentMethod.CARD, order50, -5.0)
        );
    }

    @Test
    void testTipNullAllowedInConstructor() {
        Receipt r = new Receipt(PaymentMethod.CARD, order50, null);
        assertNull(r.getTip());
    }

    @Test
    void testTipZeroAllowedInConstructor() {
        Receipt r = new Receipt(PaymentMethod.CARD, order50, 0.0);
        assertEquals(Double.valueOf(0.0), r.getTip());
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

    @Test
    void testReceiptAddedToExtent() {
        assertEquals(2, Receipt.getExtent().size());
        assertTrue(Receipt.getExtent().contains(receiptNoTip));
        assertTrue(Receipt.getExtent().contains(receiptWithTip));
    }

    @Test
    void testAddExtentWithNull() {
        assertThrows(IllegalArgumentException.class, () -> Receipt.addExtent(null));
    }

    @Test
    void testAddExtentDuplicateThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Receipt.addExtent(receiptNoTip));
    }

    @Test
    void testGetExtentIsUnmodifiable() {
        assertThrows(UnsupportedOperationException.class, () -> {
            Receipt.getExtent().add(receiptNoTip);
        });
    }

    @Test
    void testRemoveFromExtent() {
        assertTrue(Receipt.getExtent().contains(receiptNoTip));
        Receipt.removeFromExtent(receiptNoTip);
        assertFalse(Receipt.getExtent().contains(receiptNoTip));
    }

    @Test
    void testTipDoesNotAffectOtherReceipts() {
        double original = receiptNoTip.getSum();
        receiptWithTip.setTip(Double.valueOf(50.0));
        assertEquals(original, receiptNoTip.getSum());
    }

    @Test
    void testSumUpdatesWhenOrderPriceChanges() {
        Receipt r = new Receipt(PaymentMethod.CASH, order50, Double.valueOf(5.0));
        double initial = r.getSum();

        order50.addDish(new Dish("Extra", 20.0, reviews), 1);
        r.setSum();

        assertNotEquals(initial, r.getSum());
    }

    @Test
    void testTipZeroIsAllowed() {
        Receipt r = new Receipt(PaymentMethod.CARD, order50, Double.valueOf(0.0));
        assertEquals(Double.valueOf(0.0), r.getTip());
    }

    @Test
    void testChangingOrderUpdatesSum() {
        Receipt r = new Receipt(PaymentMethod.CASH, order100, null);
        double oldSum = r.getSum();

        r.setOrder(order50);
        assertNotEquals(oldSum, r.getSum());
    }

    @Test
    void testGetOrder() {
        assertEquals(order100, receiptNoTip.getOrder());
        assertEquals(order101, receiptWithTip.getOrder());
    }

    @Test
    void testMultipleReceipts() {
        Receipt r1 = new Receipt(PaymentMethod.CARD, order50);
        Receipt r2 = new Receipt(PaymentMethod.CASH, order100, Double.valueOf(5.0));

        assertEquals(4, Receipt.getExtent().size());
        assertTrue(Receipt.getExtent().contains(r1));
        assertTrue(Receipt.getExtent().contains(r2));
    }

    @Test
    void testClearExtentWorksProperly() throws Exception {
        Receipt r1 = new Receipt(PaymentMethod.CARD, order50);
        Receipt r2 = new Receipt(PaymentMethod.CASH, order100, Double.valueOf(5.0));

        assertEquals(4, Receipt.getExtent().size());

        Method clear = Receipt.class.getDeclaredMethod("clearExtent");
        clear.setAccessible(true);
        clear.invoke(null);

        assertEquals(0, Receipt.getExtent().size());
    }
}