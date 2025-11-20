package test;

import classes.Dish;
import classes.Order;
import classes.OrderStatus;
import classes.Table;
import classes.TableStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    private Order dineInOrder;
    private Table testTable;
    private LocalDateTime testTimestamp;
    private Dish pizza;
    private Dish pasta;
    private Dish salad;

    @BeforeEach
    void setUp() throws Exception {
        Field orderExtent = Order.class.getDeclaredField("orders");
        orderExtent.setAccessible(true);
        ((List<?>) orderExtent.get(null)).clear();

        Field tableExtent = Table.class.getDeclaredField("extent");
        tableExtent.setAccessible(true);
        ((List<?>) tableExtent.get(null)).clear();

        Field dishExtent = Dish.class.getDeclaredField("extent");
        dishExtent.setAccessible(true);
        ((List<?>) dishExtent.get(null)).clear();

        testTimestamp = LocalDateTime.of(2025, 11, 12, 19, 30);
        testTable = new Table(1, 4, TableStatus.AVAILABLE, testTimestamp);

        pizza = new Dish("Pizza", 12.99);
        pasta = new Dish("Pasta", 10.50);
        salad = new Dish("Salad", 7.99);

        dineInOrder = new Order(1, 4, OrderStatus.TAKEN, 3, testTimestamp, testTable);
    }

    @Test
    void testFullConstructor() {
        assertEquals(1, dineInOrder.getId());
        assertEquals(4, dineInOrder.getNumberOfPeople());
        assertEquals(OrderStatus.TAKEN, dineInOrder.getStatus());
        assertEquals(testTable, dineInOrder.getTable());
        assertNotNull(dineInOrder.getTimestamp());
    }

    @Test
    void testConstructorAddsToExtent() {
        assertEquals(1, Order.getOrders().size());
        assertTrue(Order.getOrders().contains(dineInOrder));
    }

    @Test
    void testGetId() {
        assertEquals(1, dineInOrder.getId());
    }

    @Test
    void testGetNumberOfPeople() {
        assertEquals(4, dineInOrder.getNumberOfPeople());
    }

    @Test
    void testGetStatus() {
        assertEquals(OrderStatus.TAKEN, dineInOrder.getStatus());
    }

    @Test
    void testGetTable() {
        assertEquals(testTable, dineInOrder.getTable());
    }

    @Test
    void testGetTimestamp() {
        assertNotNull(dineInOrder.getTimestamp());
    }

    @Test
    void testSetTimestamp() {
        LocalDateTime newTimestamp = LocalDateTime.of(2024, 12, 1, 10, 0);
        dineInOrder.setTimestamp(newTimestamp);
        assertEquals(newTimestamp, dineInOrder.getTimestamp());
    }

    @Test
    void testSetTimestampNull() {
        dineInOrder.setTimestamp(null);
        assertNull(dineInOrder.getTimestamp());
    }

    @Test
    void testSetStatus() {
        dineInOrder.setStatus(OrderStatus.IN_PREPARATION);
        assertEquals(OrderStatus.IN_PREPARATION, dineInOrder.getStatus());
    }

    @Test
    void testSetStatusNull() {
        assertThrows(NullPointerException.class, () -> {
            dineInOrder.setStatus(null);
        });
    }

    @Test
    void testSetTable() {
        Table newTable = new Table(2, 6, TableStatus.AVAILABLE, testTimestamp);
        dineInOrder.setTable(newTable);
        assertEquals(newTable, dineInOrder.getTable());
    }

    @Test
    void testSetTableNull() {
        assertThrows(NullPointerException.class, () -> {
            dineInOrder.setTable(null);
        });
    }

    @Test
    void testUpdateOrderStatus() {
        dineInOrder.updateOrderStatus(OrderStatus.READY);
        assertEquals(OrderStatus.READY, dineInOrder.getStatus());
    }

    @Test
    void testConstructorWithNullTable() {
        assertThrows(NullPointerException.class, () -> {
            new Order(0, 0, OrderStatus.TAKEN, 0, testTimestamp, null);
        });
    }

    @Test
    void testConstructorWithNullStatus() {
        assertThrows(NullPointerException.class, () -> {
            new Order(0, 1, null, 1, testTimestamp, testTable);
        });
    }

    @Test
    void testAddDish() {
        dineInOrder.addDish(pizza);

        assertEquals(1, dineInOrder.getDishCount());
        assertTrue(dineInOrder.containsDish(pizza));
    }

    @Test
    void testAddMultipleDishes() {
        dineInOrder.addDish(pizza);
        dineInOrder.addDish(pasta);
        dineInOrder.addDish(salad);

        assertEquals(3, dineInOrder.getDishCount());
        assertTrue(dineInOrder.containsDish(pizza));
        assertTrue(dineInOrder.containsDish(pasta));
        assertTrue(dineInOrder.containsDish(salad));
    }

    @Test
    void testAddDuplicateDish() {
        dineInOrder.addDish(pizza);
        dineInOrder.addDish(pizza);

        assertEquals(1, dineInOrder.getDishCount());
    }

    @Test
    void testAddNullDish() {
        assertThrows(IllegalArgumentException.class, () -> {
            dineInOrder.addDish(null);
        });
    }

    @Test
    void testRemoveDish() {
        dineInOrder.addDish(pizza);
        dineInOrder.addDish(pasta);

        assertEquals(2, dineInOrder.getDishCount());

        dineInOrder.removeDish(pizza);

        assertEquals(1, dineInOrder.getDishCount());
        assertFalse(dineInOrder.containsDish(pizza));
        assertTrue(dineInOrder.containsDish(pasta));
    }

    @Test
    void testRemoveNonExistentDish() {
        dineInOrder.addDish(pizza);
        dineInOrder.removeDish(pasta);
        assertEquals(1, dineInOrder.getDishCount());
    }

    @Test
    void testGetDishes() {
        dineInOrder.addDish(pizza);
        dineInOrder.addDish(pasta);

        List<Dish> dishes = dineInOrder.getDishes();

        assertEquals(2, dishes.size());
        assertTrue(dishes.contains(pizza));
        assertTrue(dishes.contains(pasta));
    }

    @Test
    void testGetDishesIsUnmodifiable() {
        dineInOrder.addDish(pizza);

        assertThrows(UnsupportedOperationException.class, () -> {
            dineInOrder.getDishes().add(pasta);
        });
    }

    @Test
    void testContainsDish() {
        dineInOrder.addDish(pizza);

        assertTrue(dineInOrder.containsDish(pizza));
        assertFalse(dineInOrder.containsDish(pasta));
    }

    @Test
    void testGetDishCount() {
        assertEquals(0, dineInOrder.getDishCount());

        dineInOrder.addDish(pizza);
        assertEquals(1, dineInOrder.getDishCount());

        dineInOrder.addDish(pasta);
        assertEquals(2, dineInOrder.getDishCount());
    }

    @Test
    void testGetTotalPriceWithNoDishes() {
        assertEquals(0.0, dineInOrder.getTotalPrice(), 0.001);
    }

    @Test
    void testGetTotalPriceWithDishes() {
        dineInOrder.addDish(pizza);
        dineInOrder.addDish(pasta);
        dineInOrder.addDish(salad);
        assertEquals(31.48, dineInOrder.getTotalPrice(), 0.01);
    }

    @Test
    void testGetTotalPriceIsStatic() {
        Order order1 = new Order(2, 2, OrderStatus.TAKEN, 1, testTimestamp, testTable);

        order1.addDish(pizza);
        order1.addDish(pasta);

        assertEquals(23.49, order1.getTotalPrice(), 0.01);
    }

    @Test
    void testAddExtent() {
        Order newOrder = new Order(5, 2, OrderStatus.IN_PREPARATION, 1, testTimestamp, testTable);

        assertEquals(2, Order.getOrders().size());
        assertTrue(Order.getOrders().contains(newOrder));
    }

    @Test
    void testAddExtentWithNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            Order.addExtent(null);
        });
    }

    @Test
    void testGetOrdersIsUnmodifiable() {
        assertThrows(UnsupportedOperationException.class, () -> {
            Order.getOrders().add(dineInOrder);
        });
    }

    @Test
    void testRemoveFromExtent() {
        assertTrue(Order.getOrders().contains(dineInOrder));

        Order.removeFromExtent(dineInOrder);
        assertFalse(Order.getOrders().contains(dineInOrder));
    }

    @Test
    void testMultipleOrders() {
        LocalDateTime time2 = LocalDateTime.of(2025, 11, 12, 20, 0);
        LocalDateTime time3 = LocalDateTime.of(2025, 11, 12, 21, 0);

        Order order2 = new Order(8, 2, OrderStatus.IN_PREPARATION, 2, time2, testTable);
        Order order3 = new Order(9, 3, OrderStatus.READY, 3, time3, testTable);

        assertEquals(3, Order.getOrders().size());
        assertTrue(Order.getOrders().contains(dineInOrder));
        assertTrue(Order.getOrders().contains(order2));
        assertTrue(Order.getOrders().contains(order3));
    }

    @Test
    void testOrderStatusEnumValues() {
        OrderStatus[] statuses = OrderStatus.values();
        assertEquals(5, statuses.length);

        assertTrue(containsStatus(statuses, OrderStatus.TAKEN));
        assertTrue(containsStatus(statuses, OrderStatus.IN_PREPARATION));
        assertTrue(containsStatus(statuses, OrderStatus.READY));
        assertTrue(containsStatus(statuses, OrderStatus.SERVED));
        assertTrue(containsStatus(statuses, OrderStatus.DELIVERED));
    }

    @Test
    void testOrderStatusValueOf() {
        assertEquals(OrderStatus.TAKEN, OrderStatus.valueOf("TAKEN"));
        assertEquals(OrderStatus.IN_PREPARATION, OrderStatus.valueOf("IN_PREPARATION"));
        assertEquals(OrderStatus.READY, OrderStatus.valueOf("READY"));
        assertEquals(OrderStatus.SERVED, OrderStatus.valueOf("SERVED"));
        assertEquals(OrderStatus.DELIVERED, OrderStatus.valueOf("DELIVERED"));
    }

    @Test
    void testOrderStatusValueOfInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            OrderStatus.valueOf("INVALID");
        });
    }

    private boolean containsStatus(OrderStatus[] statuses, OrderStatus target) {
        for (OrderStatus status : statuses) {
            if (status == target) {
                return true;
            }
        }
        return false;
    }
}