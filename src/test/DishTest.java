package test;
import classes.Dish;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DishTest {

    private Dish dish1;
    private Dish dish2;
    private Dish dish3;

    @BeforeEach
    void setUp() throws Exception {
        Field dishExtent = Dish.class.getDeclaredField("extent");
        dishExtent.setAccessible(true);
        ((List<?>) dishExtent.get(null)).clear();

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
        Dish newDish = new Dish("Dish4", 12.00);
        int initialSize = Dish.getDishList().size();

        Dish.addNewDish(newDish);

        List<Dish> dishes = Dish.getDishList();
        assertEquals(initialSize + 1, dishes.size());
        assertTrue(dishes.contains(newDish));
    }


    @Test
    void testAddMultipleDishes() {
        Dish dish4 = new Dish("Dish5", 8.00);
        Dish dish5 = new Dish("Dish6", 12.00);

        Dish.addNewDish(dish4);
        Dish.addNewDish(dish5);

        List<Dish> dishes = Dish.getDishList();
        assertTrue(dishes.contains(dish1));
        assertTrue(dishes.contains(dish2));
        assertTrue(dishes.contains(dish3));
        assertTrue(dishes.contains(dish4));
        assertTrue(dishes.contains(dish5));
    }

    @Test
    void testCheckAvailabilityExistingDish() {
        Dish.addNewDish(dish1);
        assertTrue(Dish.checkAvailability(dish1));
    }

    @Test
    void testCheckAvailabilityNonExistingDish() {
        Dish newDish = new Dish("Temp Dish", 5.00);
        Dish.deleteDish(newDish);
        assertFalse(Dish.checkAvailability(newDish));
    }

    @Test
    void testDeleteExistingDish() {
        assertTrue(Dish.getDishList().contains(dish1));
        Dish.deleteDish(dish1);
        assertFalse(Dish.getDishList().contains(dish1));
        assertTrue(Dish.getDishList().contains(dish2));
    }

    @Test
    void testDeleteNonExistingDish() {
        Dish newDish = new Dish("Temp Dish", 7.00);
        Dish.deleteDish(newDish);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Dish.deleteDish(newDish)
        );

        assertEquals("Dish could not be found", exception.getMessage());
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
        assertTrue(Dish.checkAvailability(dish1));
        Dish.deleteDish(dish1);
        assertFalse(Dish.checkAvailability(dish1));
    }

    @Test
    void testAddExtent() {
        Dish newDish = new Dish("Extent Test", 15.00);
        assertTrue(Dish.getExtent().contains(newDish));
    }

    @Test
    void testAddExtentWithNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            Dish.addExtent(null);
        });
    }

    @Test
    void testGetExtentIsUnmodifiable() {
        assertThrows(UnsupportedOperationException.class, () -> {
            Dish.getExtent().add(new Dish("Test", 10.00));
        });
    }

    @Test
    void testRemoveFromExtent() {
        Dish dishToRemove = new Dish("Temporary Dish", 8.00);
        assertTrue(Dish.getExtent().contains(dishToRemove));

        Dish.removeFromExtent(dishToRemove);
        assertFalse(Dish.getExtent().contains(dishToRemove));
    }
}
