package test;
import classes.Dish;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DishTest {

    private Dish dish1;
    private Dish dish2;
    private Dish dish3;

    @BeforeEach
    void setUp() {
        Dish.getDishList().clear();
        dish1 = new Dish("Dish1", 15.50);
        dish2 = new Dish("Dish2", 10.50);
        dish3 = new Dish("Dish3", 5.50);
    }

    @Test
    void testConstructor() {
        assertEquals("Dish1", dish1.getName());
        assertEquals(15.50, dish1.getPrice(), 0.001);
    }

    @Test
    void testAddNewDish() {
        Dish.addNewDish(dish1);

        List<Dish> dishes = Dish.getDishList();
        assertEquals(1, dishes.size());
        assertTrue(dishes.contains(dish1));
    }

    @Test
    void testAddMultipleDishes() {
        Dish.addNewDish(dish1);
        Dish.addNewDish(dish2);
        Dish.addNewDish(dish3);

        List<Dish> dishes = Dish.getDishList();
        assertEquals(3, dishes.size());
        assertTrue(dishes.contains(dish1));
        assertTrue(dishes.contains(dish2));
        assertTrue(dishes.contains(dish3));
    }

    @Test
    void testCheckAvailabilityExistingDish() {
        Dish.addNewDish(dish1);
        assertTrue(Dish.checkAvailability(dish1));
    }

    @Test
    void testCheckAvailabilityNonExistingDish() {
        Dish.addNewDish(dish1);
        assertFalse(Dish.checkAvailability(dish2));
    }

    @Test
    void testCheckAvailabilityEmptyList() {
        assertFalse(Dish.checkAvailability(dish1));
    }

    @Test
    void testDeleteExistingDish() {
        Dish.addNewDish(dish1);
        Dish.addNewDish(dish2);
        Dish.deleteDish(dish1);
        List<Dish> dishes = Dish.getDishList();
        assertEquals(1, dishes.size());
        assertFalse(dishes.contains(dish1));
        assertTrue(dishes.contains(dish2));
    }

    @Test
    void testDeleteNonExistingDish() {
        Dish.addNewDish(dish1);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Dish.deleteDish(dish2)
        );

        assertEquals("Dish could not be found", exception.getMessage());
    }

    @Test
    void testDeleteFromEmptyList() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Dish.deleteDish(dish1)
        );
    }

    @Test
    void testGetDishListEmpty() {
        List<Dish> dishes = Dish.getDishList();
        assertNotNull(dishes);
        assertEquals(0, dishes.size());
    }

    @Test
    void testGetDishListWithDishes() {
        Dish.addNewDish(dish1);
        Dish.addNewDish(dish2);
        List<Dish> dishes = Dish.getDishList();
        assertEquals(2, dishes.size());
        assertTrue(dishes.contains(dish1));
        assertTrue(dishes.contains(dish2));
    }

    @Test
    void testGetName() {
        assertEquals("Dish1", dish1.getName());
        assertEquals("Dish2", dish2.getName());
    }

    @Test
    void testGetPrice() {
        assertEquals(15.50, dish1.getPrice(), 0.001);
        assertEquals(10.50, dish2.getPrice(), 0.001);
    }

    @Test
    void testSameDishReference() {
        Dish.addNewDish(dish1);
        assertTrue(Dish.checkAvailability(dish1));
        Dish.deleteDish(dish1);
        assertFalse(Dish.checkAvailability(dish1));
    }
}
