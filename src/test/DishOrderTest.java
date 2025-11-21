package test;

import classes.Dish;
import classes.DishOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class DishOrderTest {

    @BeforeEach
    void setUp() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method clearDishOrder = DishOrder.class.getDeclaredMethod("clearExtent");
        clearDishOrder.setAccessible(true);
        clearDishOrder.invoke(null);
    }

    @Test
    void testConstructorAndGetters() {
        Dish dish = new Dish("Pizza", 15.99);
        DishOrder order = new DishOrder(dish, 2);

        assertEquals(dish, order.getDish());
        assertEquals(2, order.getQuantity());
    }

    @Test
    void testSetQuantity() {
        Dish dish = new Dish("Burger", 10.50);
        DishOrder order = new DishOrder(dish, 1);

        order.setQuantity(5);
        assertEquals(5, order.getQuantity());
    }

    @Test
    void testAddExtent() {
        Dish dish = new Dish("Pasta", 12.00);
        DishOrder order = new DishOrder(dish, 3);

        DishOrder.addExtent(order);
        assertTrue(DishOrder.getExtent().contains(order));
        assertEquals(1, DishOrder.getExtent().size());
    }

    @Test
    void testAddExtentNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            DishOrder.addExtent(null);
        });
        assertEquals("DishOrder cannot be null", exception.getMessage());
    }

    @Test
    void testAddExtentDuplicate() {
        Dish dish = new Dish("Salad", 8.50);
        DishOrder order = new DishOrder(dish, 1);

        DishOrder.addExtent(order);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            DishOrder.addExtent(order);
        });
        assertEquals("Such DishOrder is already in data base", exception.getMessage());
    }

    @Test
    void testRemoveFromExtent() {
        Dish dish = new Dish("Soup", 6.00);
        DishOrder order = new DishOrder(dish, 2);

        DishOrder.addExtent(order);
        assertEquals(1, DishOrder.getExtent().size());

        DishOrder.removeFromExtent(order);
        assertEquals(0, DishOrder.getExtent().size());
    }

    @Test
    void testGetExtentUnmodifiable() {
        Dish dish = new Dish("Steak", 25.00);
        DishOrder order = new DishOrder(dish, 1);
        DishOrder.addExtent(order);

        assertThrows(UnsupportedOperationException.class, () -> {
            DishOrder.getExtent().add(new DishOrder(dish, 2));
        });
    }

    @Test
    void testClearExtent() {
        DishOrder.addExtent(new DishOrder(new Dish("Dish1", 10.0), 1));
        DishOrder.addExtent(new DishOrder(new Dish("Dish2", 15.0), 2));

        assertEquals(2, DishOrder.getExtent().size());

        DishOrder.clearExtent();
        assertEquals(0, DishOrder.getExtent().size());
    }

    @Test
    void testMultipleOrders() {
        Dish pizza = new Dish("Pizza", 15.99);
        Dish burger = new Dish("Burger", 10.50);

        DishOrder order1 = new DishOrder(pizza, 2);
        DishOrder order2 = new DishOrder(burger, 3);

        DishOrder.addExtent(order1);
        DishOrder.addExtent(order2);

        assertEquals(2, DishOrder.getExtent().size());
        assertTrue(DishOrder.getExtent().contains(order1));
        assertTrue(DishOrder.getExtent().contains(order2));
    }
}
