package test;

import classes.Dish;
import classes.Product;
import classes.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DishTest {

    private Dish dish1;
    private Dish dish2;
    private Dish dish3;
    private List<Integer> reviews = new ArrayList<>(List.of(4, 5, 4, 5, 5, 3));
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() throws Exception {
        Method clearDish = Dish.class.getDeclaredMethod("clearExtent");
        clearDish.setAccessible(true);
        clearDish.invoke(null);

        Method clearProduct = Product.class.getDeclaredMethod("clearExtent");
        clearProduct.setAccessible(true);
        clearProduct.invoke(null);

        dish1 = new Dish("Dish1", 15.50, reviews);
        dish2 = new Dish("Dish2", 10.50, reviews);
        dish3 = new Dish("Dish3", 5.50, reviews);

        product1 = new Product(1L, "Tomato", 0.5, Category.VEGETABLES, LocalDate.now().plusDays(10), 2.50);
        product2 = new Product(2L, "Cheese", 0.3, Category.DAIRY, LocalDate.now().plusDays(30), 5.00);
    }

    @Test
    void testConstructor() {
        assertEquals("Dish1", dish1.getName());
        assertEquals(15.50, dish1.getPrice(), 0.001);
    }

    @Test
    void testAddNewDish() {
        Dish newDish = new Dish("Dish4", 12.00, reviews);
        int initialSize = Dish.getDishList().size();

        Dish.addNewDish(newDish);

        List<Dish> dishes = Dish.getDishList();
        assertEquals(initialSize + 1, dishes.size());
        assertTrue(dishes.contains(newDish));
    }

    @Test
    void testAddMultipleDishes() {
        Dish dish4 = new Dish("Dish5", 8.00, reviews);
        Dish dish5 = new Dish("Dish6", 12.00, reviews);

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
        Dish newDish = new Dish("Temp Dish", 5.00, reviews);
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
        Dish newDish = new Dish("Temp Dish", 7.00, reviews);
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
        Dish newDish = new Dish("Extent Test", 15.00, reviews);
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
            Dish.getExtent().add(new Dish("Test", 10.00, reviews));
        });
    }

    @Test
    void testRemoveFromExtent() {
        Dish dishToRemove = new Dish("Temporary Dish", 8.00, reviews);
        assertTrue(Dish.getExtent().contains(dishToRemove));

        Dish.removeFromExtent(dishToRemove);
        assertFalse(Dish.getExtent().contains(dishToRemove));
    }

    @Test
    void testClearExtent() throws Exception {
        assertEquals(3, Dish.getExtent().size());

        Method clearDish = Dish.class.getDeclaredMethod("clearExtent");
        clearDish.setAccessible(true);
        clearDish.invoke(null);

        assertEquals(0, Dish.getExtent().size());
        assertEquals(0, Dish.getDishList().size());
    }

    @Test
    void testAddProductToDishBidirectionalConnection() {
        assertTrue(product1.getDishes().isEmpty(), "Product should have no dishes");
        assertTrue(dish1.getProducts().isEmpty(), "Dish should have no products");
        dish1.addProduct(product1);
        assertTrue(product1.getDishes().contains(dish1), "Product should contain the dish after addProduct");
        assertTrue(dish1.getProducts().contains(product1), "Dish should contain the product after addProduct");
        assertEquals(1, product1.getDishes().size(), "Product should have one dish");
        assertEquals(1, dish1.getProducts().size(), "Dish should have one product");
    }

    @Test
    void testAddDishToProductBidirectionalConnection() {
        assertTrue(product1.getDishes().isEmpty(), "Product should have no dishes");
        assertTrue(dish1.getProducts().isEmpty(), "Dish should have no products");
        product1.addDish(dish1);
        assertTrue(product1.getDishes().contains(dish1), "Product should contain the dish");
        assertTrue(dish1.getProducts().contains(product1), "Dish should contain the product");
        assertEquals(1, product1.getDishes().size(), "Product should have one dish");
        assertEquals(1, dish1.getProducts().size(), "Dish should have one product");
    }

    @Test
    void testRemoveProductFromDishBidirectionalConnection() {
        dish1.addProduct(product1);
        assertTrue(product1.getDishes().contains(dish1), "Connection should be established");
        dish1.removeProduct(product1);
        assertFalse(product1.getDishes().contains(dish1), "Product should no longer contain the dish");
        assertFalse(dish1.getProducts().contains(product1), "Dish should no longer contain the product");
        assertTrue(product1.getDishes().isEmpty(), "Product's dishes set should be empty");
        assertTrue(dish1.getProducts().isEmpty(), "Dish's products set should be empty");
    }

    @Test
    void testMultipleProductsAndDishesAssociation() {
        dish1.addProduct(product1);
        dish1.addProduct(product2);
        product1.addDish(dish2);

        assertEquals(2, dish1.getProducts().size(), "Dish1 should have two products");
        assertTrue(dish1.getProducts().contains(product1), "Dish1 should contain product1");
        assertTrue(dish1.getProducts().contains(product2), "Dish1 should contain product2");

        assertEquals(2, product1.getDishes().size(), "Product1 should have two dishes");
        assertTrue(product1.getDishes().contains(dish1), "Product1 should contain dish1");
        assertTrue(product1.getDishes().contains(dish2), "Product1 should contain dish2");

        assertEquals(1, product2.getDishes().size(), "Product2 should have one dish");
        assertTrue(product2.getDishes().contains(dish1), "Product2 should contain dish1");
    }
}