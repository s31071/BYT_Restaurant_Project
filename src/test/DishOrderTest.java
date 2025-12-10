package test;

import classes.Dish;
import classes.DishOrder;
import classes.Order;
import classes.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DishOrderTest {
    private List<Integer> reviews = new ArrayList<>(List.of(4, 5, 4, 5, 5, 3));
    private Order testOrder;
    private LocalDateTime testTimestamp;

    @BeforeEach
    void setUp() throws Exception {
        Method clearOrder = Order.class.getDeclaredMethod("clearExtent");
        clearOrder.setAccessible(true);
        clearOrder.invoke(null);

        Method clearDish = Dish.class.getDeclaredMethod("clearExtent");
        clearDish.setAccessible(true);
        clearDish.invoke(null);

        Method clearDishOrder = DishOrder.class.getDeclaredMethod("clearExtent");
        clearDishOrder.setAccessible(true);
        clearDishOrder.invoke(null);

        testTimestamp = LocalDateTime.of(2025, 11, 12, 19, 30);
        testOrder = new Order(1, 4, OrderStatus.TAKEN, testTimestamp);
    }

    @Test
    void testConstructorAndGetters() {
        Dish dish = new Dish("Pizza", 15.99, reviews);
        DishOrder dishOrder = new DishOrder(dish, testOrder, 2);

        assertEquals(dish, dishOrder.getDish());
        assertEquals(testOrder, dishOrder.getOrder());
        assertEquals(2, dishOrder.getQuantity());
    }

    @Test
    void testSetQuantity() {
        Dish dish = new Dish("Burger", 10.50, reviews);
        DishOrder dishOrder = new DishOrder(dish, testOrder, 1);

        dishOrder.setQuantity(5);
        assertEquals(5, dishOrder.getQuantity());
    }

    @Test
    void testSetQuantityInvalid() {
        Dish dish = new Dish("Pasta", 12.00, reviews);
        DishOrder dishOrder = new DishOrder(dish, testOrder, 3);

        assertThrows(IllegalArgumentException.class, () -> {
            dishOrder.setQuantity(-1);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            dishOrder.setQuantity(0);
        });
    }

    @Test
    void testAddExtentAutomatic() {
        Dish dish = new Dish("Pasta", 12.00, reviews);
        DishOrder dishOrder = new DishOrder(dish, testOrder, 3);

        assertTrue(DishOrder.getExtent().contains(dishOrder));
        assertEquals(1, DishOrder.getExtent().size());
    }

    @Test
    void testAddExtentManual() {
        Dish dish = new Dish("Salad", 8.50, reviews);
        DishOrder dishOrder = new DishOrder(dish, testOrder, 1);
        assertTrue(DishOrder.getExtent().contains(dishOrder));
        assertEquals(1, DishOrder.getExtent().size());
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            DishOrder.addExtent(dishOrder);
        });
        assertEquals("Such DishOrder is already in data base", exception.getMessage());
    }

    @Test
    void testAddExtentNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            DishOrder.addExtent(null);
        });
        assertEquals("DishOrder cannot be null", exception.getMessage());
    }

    @Test
    void testRemoveFromExtent() {
        Dish dish = new Dish("Soup", 6.00, reviews);
        DishOrder dishOrder = new DishOrder(dish, testOrder, 2);

        assertEquals(1, DishOrder.getExtent().size());
        assertTrue(DishOrder.getExtent().contains(dishOrder));

        DishOrder.removeFromExtent(dishOrder);
        assertEquals(0, DishOrder.getExtent().size());
        assertFalse(DishOrder.getExtent().contains(dishOrder));
    }

    @Test
    void testRemoveFromExtentCleansUpAssociations() {
        Dish dish = new Dish("Steak", 25.00, reviews);
        DishOrder dishOrder = new DishOrder(dish, testOrder, 1);
        assertTrue(DishOrder.getExtent().contains(dishOrder));

        assertTrue(testOrder.getDishOrders().contains(dishOrder));
        assertTrue(dish.getDishOrders().contains(dishOrder));

        DishOrder.removeFromExtent(dishOrder);

        dish.addDishOrderDish(dishOrder);
        dish.removeDishOrderDish(dishOrder);

        assertFalse(DishOrder.getExtent().contains(dishOrder));

        assertFalse(testOrder.getDishOrders().contains(dishOrder));
        assertFalse(dish.getDishOrders().contains(dishOrder));
    }

    @Test
    void testGetExtentUnmodifiable() {
        Dish dish = new Dish("Steak", 25.00, reviews);
        Dish dish2 = new Dish("Chicken", 20.00, reviews);
        DishOrder dishOrder = new DishOrder(dish, testOrder, 1);

        assertThrows(UnsupportedOperationException.class, () -> {
            DishOrder.getExtent().add(new DishOrder(dish2, testOrder, 2));
        });
    }


    @Test
    void testClearExtent() {
        Dish dish1 = new Dish("Dish1", 10.0, reviews);
        Dish dish2 = new Dish("Dish2", 15.0, reviews);
        DishOrder dishOrder1 = new DishOrder(dish1, testOrder, 1);
        DishOrder dishOrder2 = new DishOrder(dish2, testOrder, 2);

        assertEquals(2, DishOrder.getExtent().size());

        DishOrder.clearExtent();
        assertEquals(0, DishOrder.getExtent().size());
    }

    @Test
    void testMultipleDishOrders() {
        Dish pizza = new Dish("Pizza", 15.99, reviews);
        Dish burger = new Dish("Burger", 10.50, reviews);

        DishOrder dishOrder1 = new DishOrder(pizza, testOrder, 2);
        DishOrder dishOrder2 = new DishOrder(burger, testOrder, 3);

        assertEquals(2, DishOrder.getExtent().size());
        assertTrue(DishOrder.getExtent().contains(dishOrder1));
        assertTrue(DishOrder.getExtent().contains(dishOrder2));
    }

    @Test
    void testReverseConnectionWithDish() {
        Dish dish = new Dish("Pizza", 15.99, reviews);
        DishOrder dishOrder = new DishOrder(dish, testOrder, 2);
        assertTrue(dish.getDishOrders().contains(dishOrder));
        assertEquals(1, dish.getDishOrders().size());
    }

    @Test
    void testReverseConnectionWithOrder() {
        Dish dish = new Dish("Pizza", 15.99, reviews);
        DishOrder dishOrder = new DishOrder(dish, testOrder, 2);
        assertTrue(testOrder.getDishOrders().contains(dishOrder));
        assertEquals(1, testOrder.getDishOrders().size());
    }

    @Test
    void testBidirectionalConnection() {
        Dish dish = new Dish("Pizza", 15.99, reviews);
        DishOrder dishOrder = new DishOrder(dish, testOrder, 2);
        assertTrue(dish.getDishOrders().contains(dishOrder));
        assertTrue(testOrder.getDishOrders().contains(dishOrder));
        assertEquals(dish, dishOrder.getDish());
        assertEquals(testOrder, dishOrder.getOrder());
        assertEquals(2, dishOrder.getQuantity());
    }

    @Test
    void testMultipleDishOrdersInDish() {
        Dish dish = new Dish("Pizza", 15.99, reviews);
        Order order1 = new Order(2, 2, OrderStatus.TAKEN, testTimestamp);
        Order order2 = new Order(3, 3, OrderStatus.TAKEN, testTimestamp);

        DishOrder dishOrder1 = new DishOrder(dish, testOrder, 1);
        DishOrder dishOrder2 = new DishOrder(dish, order1, 2);
        DishOrder dishOrder3 = new DishOrder(dish, order2, 3);
        assertEquals(3, dish.getDishOrders().size());
        assertTrue(dish.getDishOrders().contains(dishOrder1));
        assertTrue(dish.getDishOrders().contains(dishOrder2));
        assertTrue(dish.getDishOrders().contains(dishOrder3));
    }

    @Test
    void testMultipleDishOrdersInOrder() {
        Dish dish1 = new Dish("Pizza", 15.99, reviews);
        Dish dish2 = new Dish("Burger", 10.50, reviews);
        Dish dish3 = new Dish("Salad", 8.99, reviews);

        DishOrder dishOrder1 = new DishOrder(dish1, testOrder, 1);
        DishOrder dishOrder2 = new DishOrder(dish2, testOrder, 2);
        DishOrder dishOrder3 = new DishOrder(dish3, testOrder, 3);
        assertEquals(3, testOrder.getDishOrders().size());
        assertTrue(testOrder.getDishOrders().contains(dishOrder1));
        assertTrue(testOrder.getDishOrders().contains(dishOrder2));
        assertTrue(testOrder.getDishOrders().contains(dishOrder3));
    }

    @Test
    void testSetDishNullThrowsException() {
        Dish dish = new Dish("Pizza", 15.99, reviews);
        DishOrder dishOrder = new DishOrder(dish, testOrder, 2);

        assertThrows(IllegalArgumentException.class, () -> {
            dishOrder.setDish(null);
        });
    }

    @Test
    void testSetOrderNullThrowsException() {
        Dish dish = new Dish("Pizza", 15.99, reviews);
        DishOrder dishOrder = new DishOrder(dish, testOrder, 2);

        assertThrows(IllegalArgumentException.class, () -> {
            dishOrder.setOrder(null);
        });
    }

    @Test
    void testDishOrderExtentPersistence() {
        Dish dish1 = new Dish("Dish1", 10.0, reviews);
        Dish dish2 = new Dish("Dish2", 15.0, reviews);

        DishOrder dishOrder1 = new DishOrder(dish1, testOrder, 1);
        DishOrder dishOrder2 = new DishOrder(dish2, testOrder, 2);

        assertEquals(2, DishOrder.getExtent().size());
        DishOrder.removeFromExtent(dishOrder1);

        assertEquals(1, DishOrder.getExtent().size());
        assertFalse(DishOrder.getExtent().contains(dishOrder1));
        assertTrue(DishOrder.getExtent().contains(dishOrder2));
    }

    @Test
    void testDishOrderGettersAfterConstruction() {
        Dish dish = new Dish("Risotto", 18.50, reviews);
        DishOrder dishOrder = new DishOrder(dish, testOrder, 5);
        assertEquals(dish, dishOrder.getDish());
        assertEquals(testOrder, dishOrder.getOrder());
        assertEquals(5, dishOrder.getQuantity());
    }
}