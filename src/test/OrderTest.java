package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    private Order dineInOrder;
    private Table testTable;
    private LocalDateTime testTimestamp;
    private Dish pizza;
    private Dish pasta;
    private Dish salad;
    private List<Integer> reviews = new ArrayList<>(List.of(4, 5, 4, 5, 5, 3));

    @BeforeEach
    void setUp() throws Exception {

        Method clearOrder = Order.class.getDeclaredMethod("clearExtent");
        clearOrder.setAccessible(true);
        clearOrder.invoke(null);

        Method clearDish = Dish.class.getDeclaredMethod("clearExtent");
        clearDish.setAccessible(true);
        clearDish.invoke(null);

        Method clearTable = Table.class.getDeclaredMethod("clearExtent");
        clearTable.setAccessible(true);
        clearTable.invoke(null);

        Method clearDishOrder = DishOrder.class.getDeclaredMethod("clearExtent");
        clearDishOrder.setAccessible(true);
        clearDishOrder.invoke(null);

        testTimestamp = LocalDateTime.of(2025, 11, 12, 19, 30);
        testTable = new Table(1, 4, TableStatus.AVAILABLE, testTimestamp);

        pizza = new Dish("Pizza", 12.99, reviews);
        pasta = new Dish("Pasta", 10.50, reviews);
        salad = new Dish("Salad", 7.99, reviews);

        dineInOrder = new Order(1, 4, OrderStatus.TAKEN, testTimestamp, testTable);

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
            new Order(2, 2, OrderStatus.TAKEN, testTimestamp, null);
        });
    }

    @Test
    void testConstructorWithNullStatus() {
        assertThrows(NullPointerException.class, () -> {
            new Order(3, 1, null, testTimestamp, testTable);
        });
    }

    @Test
    void testAddDish() {
        dineInOrder.addDish(pizza, 1);

        assertEquals(1, dineInOrder.getDishCount());
        assertTrue(dineInOrder.containsDish(pizza));
    }

    @Test
    void testAddMultipleDishes() {
        dineInOrder.addDish(pizza, 1);
        dineInOrder.addDish(pasta, 2);
        dineInOrder.addDish(salad, 2);

        assertEquals(3, dineInOrder.getDishCount());
        assertTrue(dineInOrder.containsDish(pizza));
        assertTrue(dineInOrder.containsDish(pasta));
        assertTrue(dineInOrder.containsDish(salad));
    }

    @Test
    void testAddDuplicateDish() {
        dineInOrder.addDish(pizza, 1);
        dineInOrder.addDish(pizza, 2);
        assertEquals(1, dineInOrder.getDishCount());
        Set<DishOrder> dishOrders = dineInOrder.getDishOrders();
        DishOrder dishOrder = dishOrders.stream().findFirst().orElse(null);
        assertNotNull(dishOrder, "DishOrder should exist");
        assertEquals(3, dishOrder.getQuantity());
    }

    @Test
    void testAddNullDish() {
        assertThrows(IllegalArgumentException.class, () -> {
            dineInOrder.addDish(null, 1);
        });
    }

    @Test
    void testRemoveDish() {
        dineInOrder.addDish(pizza, 2);
        dineInOrder.addDish(pasta, 1);

        assertEquals(2, dineInOrder.getDishCount());

        dineInOrder.removeDish(pizza);

        assertEquals(1, dineInOrder.getDishCount());
        assertFalse(dineInOrder.containsDish(pizza));
        assertTrue(dineInOrder.containsDish(pasta));
    }

    @Test
    void testRemoveNonExistentDish() {
        dineInOrder.addDish(pizza, 3);
        dineInOrder.removeDish(pasta);
        assertEquals(1, dineInOrder.getDishCount());
    }

    @Test
    void testGetDishes() {
        dineInOrder.addDish(pizza, 3);
        dineInOrder.addDish(pasta, 4);

        Set<DishOrder> dishes = dineInOrder.getDishOrders();

        assertEquals(2, dishes.size());
        boolean hasPizza = false;
        boolean hasPasta = false;
        for (DishOrder dishOrder : dishes) {
            if (dishOrder.getDish().equals(pizza)) {
                hasPizza = true;
            }
            if (dishOrder.getDish().equals(pasta)) {
                hasPasta = true;
            }
        }
        assertTrue(hasPizza);
        assertTrue(hasPasta);
    }

    @Test
    void testGetDishesIsUnmodifiable() {
        dineInOrder.addDish(pizza, 1);

        assertThrows(UnsupportedOperationException.class, () -> {
            dineInOrder.getDishOrders().add(new DishOrder(pasta, dineInOrder, 1));
        });
    }

    @Test
    void testContainsDish() {
        dineInOrder.addDish(pizza, 1);

        assertTrue(dineInOrder.containsDish(pizza));
        assertFalse(dineInOrder.containsDish(pasta));
    }


    @Test
    void testGetDishCount() {
        assertEquals(0, dineInOrder.getDishCount());

        dineInOrder.addDish(pizza, 2);
        assertEquals(1, dineInOrder.getDishCount());

        dineInOrder.addDish(pasta, 1);
        assertEquals(2, dineInOrder.getDishCount());
    }

    @Test
    void testGetTotalPriceWithNoDishes() {
        assertEquals(0.0, dineInOrder.getTotalPrice(), 0.001);
    }

    @Test
    void testGetTotalPriceWithDishes() {
        dineInOrder.addDish(pizza, 1);
        dineInOrder.addDish(pasta, 2);
        dineInOrder.addDish(salad, 3);
        assertEquals(57.96, dineInOrder.getTotalPrice(), 0.01);
    }

    @Test
    void testGetTotalPriceIsNotStatic() {
        Order order1 = new Order(2, 2, OrderStatus.TAKEN, testTimestamp, testTable);
        Order order2 = new Order(3, 3, OrderStatus.TAKEN, testTimestamp, testTable);

        order1.addDish(pizza, 1);
        order1.addDish(pasta, 1);

        order2.addDish(salad, 2);

        assertEquals(23.49, order1.getTotalPrice(), 0.01);
        assertEquals(15.98, order2.getTotalPrice(), 0.01);
    }

    @Test
    void testAddExtent() {
        Order newOrder = new Order(5, 2, OrderStatus.IN_PREPARATION, testTimestamp, testTable);

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

        Order order2 = new Order(8, 2, OrderStatus.IN_PREPARATION, time2, testTable);
        Order order3 = new Order(9, 3, OrderStatus.READY, time3, testTable);

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

    @Test
    void testClearExtent() throws Exception {
        assertEquals(1, Order.getOrders().size());

        Method clearOrder = Order.class.getDeclaredMethod("clearExtent");
        clearOrder.setAccessible(true);
        clearOrder.invoke(null);

        assertEquals(0, Order.getOrders().size());
    }

    private boolean containsStatus(OrderStatus[] statuses, OrderStatus target) {
        for (OrderStatus status : statuses) {
            if (status == target) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void testOrderDishConnection_AddDish() {
        Order order = new Order(1, 2, OrderStatus.IN_PREPARATION, LocalDateTime.now());
        Dish dish = new Dish("Risotto", 15.99, Arrays.asList(4, 5));

        order.addDish(dish, 2);
        assertTrue(order.containsDish(dish));
        assertEquals(1, order.getDishCount());
        DishOrder dishOrder = order.getDishOrders().stream().findFirst().orElse(null);
        assertNotNull(dishOrder, "DishOrder should exist");
        assertEquals(2, dishOrder.getQuantity());
    }

    @Test
    public void testOrderDishConnection_AddDishIncrementsQuantity() {
        Order order = new Order(1, 2, OrderStatus.READY, LocalDateTime.now());
        Dish dish = new Dish("Farfalle", 15.99, Arrays.asList(4, 5));

        order.addDish(dish, 2);
        order.addDish(dish, 3);

        assertEquals(1, order.getDishCount());

        DishOrder dishOrder = order.getDishOrders().stream().findFirst().orElse(null);
        assertNotNull(dishOrder, "DishOrder should exist");
        assertEquals(5, dishOrder.getQuantity(), "Quantity should be 5");
    }

    @Test
    public void testOrderDishConnection_RemoveDish() {
        Order order = new Order(1, 2, OrderStatus.DELIVERED, LocalDateTime.now());
        Dish dish = new Dish("Spaghetti", 15.99, Arrays.asList(4, 5));

        order.addDish(dish, 2);
        order.removeDish(dish);

        assertFalse(order.containsDish(dish));
        assertEquals(0, order.getDishCount());
    }

    @Test
    public void testOrderDishConnection_MultipleDishes() {
        Order order = new Order(1, 2, OrderStatus.IN_PREPARATION, LocalDateTime.now());
        Dish dish1 = new Dish("Carbonara", 15.99, Arrays.asList(4, 5));
        Dish dish2 = new Dish("Bolognese", 12.99, Arrays.asList(5, 4));

        order.addDish(dish1, 2);
        order.addDish(dish2, 1);

        assertEquals(2, order.getDishCount());
        assertTrue(order.containsDish(dish1));
        assertTrue(order.containsDish(dish2));
    }

    @Test
    public void testOrderDishConnection_TotalPrice() {
        Order order = new Order(1, 2, OrderStatus.TAKEN, LocalDateTime.now());
        Dish dish1 = new Dish("Kolanka", 10.0, Arrays.asList(4, 5));
        Dish dish2 = new Dish("Penne", 15.0, Arrays.asList(5, 4));

        order.addDish(dish1, 2);
        order.addDish(dish2, 1);

        assertEquals(35.0, order.getTotalPrice(), 0.01);
    }
}